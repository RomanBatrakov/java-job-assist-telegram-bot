package com.batrakov.service.jobSiteService;

import com.batrakov.entity.Vacancy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Класс Habr представляет собой реализацию интерфейса JobSite, предназначенного для
 * получения вакансий с сайта <a href="https://career.habr.com">...</a> по определенному поисковому запросу.
 * Класс осуществляет отправку HTTP-запросов и парсинг HTML-ответов для извлечения информации о вакансиях.
 *
 * @version 1.0
 * @see JobSite
 * @see Vacancy
 */
@Slf4j
@Service
public class Habr implements JobSite {
    private static final String BASE_URL = "https://career.habr.com";
    private static final String SEARCH_QUERY = "java";
    private static final String SORT_PARAM = "date";
    private static final String TYPE_PARAM = "all";
    private static final int PAGE_COUNT = 20;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Метод для получения списка вакансий с сайта Habr.
     *
     * @return Список вакансий
     * @throws RuntimeException Если возникли ошибки при отправке запросов или парсинге ответов
     */
    public List<Vacancy> getVacancies() {
        List<CompletableFuture<List<Vacancy>>> futures = new ArrayList<>();

        for (int i = 1; i <= PAGE_COUNT; i++) {
            String url =
                    String.format("%s/vacancies?page=%d&q=%s&sort=%s&type=%s", BASE_URL, i, SEARCH_QUERY, SORT_PARAM,
                            TYPE_PARAM);
            Request request = new Request.Builder().url(url).get().build();

            CompletableFuture<List<Vacancy>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Response responsePart = httpClient.newCall(request).execute();
                    if (responsePart.isSuccessful()) {
                        return responseMapper(responsePart.body().string());
                    } else {
                        log.error("Wrong request to Habr.");
                        throw new RuntimeException("Wrong request to Habr.");
                    }
                } catch (IOException e) {
                    log.error("Failed to get vacancies from Habr.", e);
                    throw new RuntimeException("Failed to get vacancies from Habr: " + e.getMessage());
                }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Vacancy>> mergedFuture = allFutures.thenApply(
                v -> futures.stream().map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList()));
        return mergedFuture.join();
    }

    /**
     * Метод для маппинга HTML-ответа на объекты вакансий.
     *
     * @param response HTML-ответ
     * @return Список объектов вакансий
     * @throws RuntimeException Если возникли ошибки при парсинге HTML-ответа
     */
    @Override
    public synchronized List<Vacancy> responseMapper(String response) {
        Elements body;
        try {
            Document doc = Jsoup.parse(response);
            body = doc.select(".vacancy-card");
        } catch (Exception e) {
            log.error("Cant parse from Habr.", e);
            throw new RuntimeException("Cant parse from Habr" + e.getMessage());
        }

        List<Vacancy> vacancies = new ArrayList<>();
        for (Element element : body) {
            Element divElement = element.selectFirst(".vacancy-card__title");
            if (divElement != null && checkVacancyDate(element)) {
                Element aElement = divElement.selectFirst("a");
                if (aElement != null) {
                    String url = aElement.attr("href");
                    String title = aElement.text();
                    Vacancy vacancy = new Vacancy();
                    vacancy.setName(title);
                    vacancy.setUrl(BASE_URL + url);
                    vacancies.add(vacancy);
                }
            }
        }
        return vacancies;
    }

    /**
     * Метод для проверки даты публикации вакансии на сайте Habr.
     *
     * @param element Элемент HTML, представляющий вакансию на сайте Habr.
     * @return true, если дата публикации вакансии соответствует текущей дате или предыдущему дню, иначе false.
     * @throws RuntimeException при ошибке парсинга даты.
     */
    private boolean checkVacancyDate(Element element) {
        Element dateElement = element.selectFirst(".vacancy-card__date");
        if (dateElement != null) {
            Element timeElement = dateElement.selectFirst("time");
            if (timeElement != null) {
                String datetime = timeElement.attr("datetime");
                try {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(datetime, FORMATTER);
                    LocalDate parsedDate = zonedDateTime.toLocalDate();
                    LocalDate currentDate = LocalDate.now();
                    return parsedDate.isEqual(currentDate) || parsedDate.isEqual(currentDate.minusDays(1));
                } catch (DateTimeParseException e) {
                    log.error("Date parse error (habr)", e);
                    throw new RuntimeException("Date parse error: " + e.getMessage());
                }
            }
        }
        return false;
    }
}
