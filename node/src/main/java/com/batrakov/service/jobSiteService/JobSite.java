package com.batrakov.service.jobSiteService;

import com.batrakov.entity.Vacancy;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Интерфейс JobSite представляет собой взаимодействие с веб-сайтом работы
 * для получения списка вакансий и их маппинга из JSON-ответа API сайта работы.
 *
 * <p>Данный интерфейс предоставляет методы для выполнения HTTP-запросов,
 * получения и маппинга данных о вакансиях с сайта работы.</p>
 *
 * <p>Примеры реализации данного интерфейса могут включать взаимодействие
 * с различными API сайтов работы, такими как HeadHunter, Habr, Proglib и другими.</p>
 *
 * <p>Для использования данного интерфейса необходимо реализовать методы
 * {@link #getVacancies()} и {@link #responseMapper(String)}, а также можно
 * переопределить метод {@link #getResponse(HttpClient, HttpRequest)} для
 * выполнения HTTP-запросов с использованием кастомного HttpClient и HttpRequest.</p>
 *
 * @version 1.0
 * @see Vacancy
 * @see HttpClient
 * @see HttpRequest
 */
public interface JobSite {
    /**
     * Получает список вакансий с сайта работы.
     *
     * @return Список объектов Vacancy, представляющих вакансии.
     */
    List<Vacancy> getVacancies();

    /**
     * Маппит JSON-ответ от API сайта работы в список объектов Vacancy.
     *
     * @param response JSON-ответ от API сайта работы в виде строки.
     * @return Список объектов Vacancy, представляющих вакансии.
     */
    List<Vacancy> responseMapper(String response);

    /**
     * Выполняет HTTP-запрос с использованием переданного HttpClient и HttpRequest,
     * и возвращает результат в виде списка объектов Vacancy.
     *
     * @param httpClient HttpClient для выполнения HTTP-запросов.
     * @param request    HttpRequest для отправки HTTP-запроса.
     * @return Список объектов Vacancy, представляющих вакансии.
     * @throws RuntimeException Если произошла ошибка при выполнении HTTP-запроса.
     */
    default List<Vacancy> getResponse(HttpClient httpClient, HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return responseMapper(response.body());
            } else {
                throw new RuntimeException("Wrong request");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
