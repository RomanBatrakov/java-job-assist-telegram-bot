package com.batrakov.service.jobSiteService;

import com.batrakov.entity.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс `Proglib` реализует интерфейс `JobSite` и представляет собой веб-сайт с вакансиями работы
 * <a href="https://proglib.io">...</a>.
 * Класс предоставляет функциональность по получению списка вакансий с Proglib и их преобразованию в объекты `Vacancy`.
 *
 * @version 1.0
 * @see JobSite
 * @see Vacancy
 */
@Slf4j
@Service
public class Proglib implements JobSite {
    private static final String PROGLIB_BASE_URL = "https://proglib.io";

    /**
     * Получает список вакансий с веб-сайта Proglib.
     *
     * @return список вакансий с Proglib
     */
    @Override
    public List<Vacancy> getVacancies() {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = PROGLIB_BASE_URL + "/vacancies/all?direction=Programming&tags%5B%5D=47511cec-0b77-49f8-9e2d" +
                "-ddf9c987c08c&workType=all&workPlace=all&experience=&salaryFrom=&page=1";
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        return getResponse(httpClient, request);
    }

    /**
     * Преобразует HTML-ответ от Proglib в список объектов Vacancy.
     *
     * @param response HTML-ответ от Proglib
     * @return список объектов Vacancy
     */
    @Override
    public List<Vacancy> responseMapper(String response) {
        Elements body;
        try {
            Document doc = Jsoup.parse(response);
            body = doc.select(".preview-card__content");
        } catch (Exception e) {
            log.error("Cant parse from proglib.", e);
            throw new RuntimeException("Cant parse from proglib" + e.getMessage());
        }

        List<Vacancy> vacancies = new ArrayList<>();
        for (Element element : body) {
            if (checkVacancyDate(element)) {
                String url = element.selectFirst("a").attr("href");
                String title = element.selectFirst("h2.preview-card__title").text();

                Vacancy vacancy = new Vacancy();
                vacancy.setName(title);
                vacancy.setUrl(PROGLIB_BASE_URL + url);
                vacancies.add(vacancy);
            }
        }
        return vacancies;
    }

    /**
     * Проверяет дату размещения вакансии на Proglib.
     *
     * @param element элемент вакансии
     * @return true, если дата размещения равна текущей дате или предыдущему дню, иначе false
     */
    private boolean checkVacancyDate(Element element) {
        if (element != null) {
            String date = element.selectFirst("div[itemprop=datePosted]").text();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return parsedDate.isEqual(LocalDate.now()) || parsedDate.isEqual(LocalDate.now().minusDays(1));
        }
        return false;
    }
}
