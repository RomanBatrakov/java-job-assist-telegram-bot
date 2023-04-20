package com.batrakov.service.jobSiteService;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс JobSiteFactory представляет фабрику для создания объектов JobSite
 * на основе переданного типа.
 *
 * <p>Данный класс предоставляет метод createJobSite(), который создает объект
 * JobSite на основе переданного типа и возвращает его. Класс также хранит
 * список доступных JobSite в виде Map, где ключ - это название класса
 * в нижнем регистре, а значение - объект JobSite.</p>
 *
 * <p>Пример использования данной фабрики может быть в контексте создания
 * различных объектов JobSite, например, для разных веб-сайтов работы.</p>
 *
 * @version 1.0
 * @see JobSite
 */
@Component
public class JobSiteFactory {
    private final Map<String, JobSite> jobSites;

    /**
     * Конструктор класса JobSiteFactory.
     *
     * @param jobSiteList Список объектов JobSite, которые будут использоваться
     *                    для создания фабрикой.
     */
    public JobSiteFactory(List<JobSite> jobSiteList) {
        this.jobSites = new HashMap<>();
        for (JobSite jobSite : jobSiteList) {
            if (jobSite != null) {
                jobSites.put(jobSite.getClass().getSimpleName().toLowerCase(), jobSite);
            }
        }
    }

    /**
     * Создает объект JobSite на основе переданного типа.
     *
     * @param type Тип объекта JobSite, который нужно создать.
     * @return Объект JobSite, созданный на основе переданного типа.
     * @throws IllegalArgumentException Если указанный тип JobSite не найден.
     */
    public JobSite createJobSite(String type) {
        JobSite jobSite = jobSites.get(type.toLowerCase());
        if (jobSite == null) {
            throw new IllegalArgumentException(String.format("Service with type=%s is not found", type));
        }
        return jobSite;
    }
}
