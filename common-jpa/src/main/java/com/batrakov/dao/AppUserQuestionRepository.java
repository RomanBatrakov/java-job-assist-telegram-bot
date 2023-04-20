package com.batrakov.dao;

import com.batrakov.entity.AppUser;
import com.batrakov.entity.AppUserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью AppUserQuestion.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью AppUserQuestion в базе данных.
 *
 * @version 1.0
 */
public interface AppUserQuestionRepository extends JpaRepository<AppUserQuestion, Long> {
    /**
     * Найти вопрос по тексту и объекту AppUser.
     *
     * @param messageText Текст вопроса, который необходимо найти
     * @param appUser     Объект AppUser, с которым связан вопрос
     * @return Optional объект с найденным вопросом или пустым значением, если вопрос не найден
     */
    Optional<AppUserQuestion> findByQuestionAndAppUser(String messageText, AppUser appUser);
}
