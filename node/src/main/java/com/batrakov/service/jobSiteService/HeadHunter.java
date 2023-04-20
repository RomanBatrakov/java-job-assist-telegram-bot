package com.batrakov.service.jobSiteService;

import com.batrakov.entity.Vacancy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс HeadHunter представляет собой реализацию интерфейса JobSite для взаимодействия с API HeadHunter и получения
 * списка вакансий.
 * Класс выполняет HTTP-запросы к API HeadHunter, получает ответы в формате JSON, и маппит их в объекты класса Vacancy.
 * Для выполнения HTTP-запросов используется Java HttpClient.
 * Для маппинга JSON-ответов в объекты используется библиотека Jackson.
 * Класс также содержит методы для настройки параметров запроса, таких как фильтры по опыту, профессиональной роли,
 * текстовому поиску и т. д.
 * Для работы с API HeadHunter необходимо передать авторизационный токен и настройки заголовков запроса, такие как
 * Content-Type и User-Agent.
 * Реализует методы интерфейса JobSite: getVacancies() и responseMapper().
 *
 * @version 1.0
 * @see JobSite
 * @see Vacancy
 */
@Slf4j
@Service
public class HeadHunter implements JobSite {
    @Value("${HH_TOKEN}")
    private String hhToken;
    @Value("${HH_EMAIL}")
    private String hhEmail;
    @Value("${JJAB_USERNAME}")
    private String botName;

    /**
     * Метод для получения вакансий с сайта HeadHunter.
     *
     * @return Список объектов Vacancy, представляющих вакансии.
     */
    @Override
    public List<Vacancy> getVacancies() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String HH_GET_VACANCIES_URL = "https://api.hh.ru/vacancies";
        URI uri = UriComponentsBuilder.fromUriString(HH_GET_VACANCIES_URL)
                                      .queryParam("experience", "noExperience")
                                      .queryParam("experience", "between1And3")
                                      .queryParam("period", "2")
                                      .queryParam("order_by", "publication_time")
                                      .queryParam("professional_role", "96")
                                      .queryParam("text", "java")
                                      .queryParam("resume_search_logic", "all")
                                      .queryParam("per_page", "100")
                                      .build()
                                      .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(uri)
                                         .header("Content-Type", "application/json")
                                         .header("Authorization", "Bearer " + hhToken)
                                         .header("HH-User-Agent", botName + " (" + hhEmail + ")")
                                         .GET()
                                         .build();
        return getResponse(httpClient, request);
    }

    /**
     * Метод для маппинга ответа от HeadHunter в список объектов Vacancy.
     *
     * @param response Ответ от HeadHunter в виде строки JSON.
     * @return Список объектов Vacancy, представляющих вакансии.
     */
    @Override
    public List<Vacancy> responseMapper(String response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode itemsNode = jsonNode.get("items");

        List<Vacancy> vacancies = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            String name = itemNode.get("name").asText();
            String url = itemNode.get("alternate_url").asText();
            Vacancy vacancy = new Vacancy();
            vacancy.setName(name);
            vacancy.setUrl(url);
            vacancies.add(vacancy);
        }
        return vacancies;
    }
}
