package com.batrakov.dao;

import com.batrakov.entity.AppUser;
import com.batrakov.entity.AppUserVacancy;
import com.batrakov.enums.VacancyState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью AppUserVacancy.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью AppUserVacancy в базе данных.
 *
 * @version 1.0
 */
public interface AppUserVacancyRepository extends JpaRepository<AppUserVacancy, Long> {
    /**
     * Найти запись о вакансии пользователя по URL вакансии и пользователю-владельцу.
     *
     * @param url     URL вакансии
     * @param appUser Пользователь, которому принадлежит запись о вакансии
     * @return Optional объект с найденной записью о вакансии или пустым значением, если запись не найдена
     */
    Optional<AppUserVacancy> findByUrlAndAppUser(String url, AppUser appUser);

    /**
     * Найти список записей о вакансиях пользователя по пользователю и состоянию вакансии.
     *
     * @param appUser      Пользователь, чьи вакансии необходимо найти
     * @param vacancyState Состояние вакансии
     * @return Список записей о вакансиях, соответствующих заданным параметрам
     */
    List<AppUserVacancy> findByAppUserAndVacancyState(AppUser appUser, VacancyState vacancyState);
}
