package com.batrakov.dao;

import com.batrakov.entity.AppCard;
import com.batrakov.enums.CardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью AppCard.
 *
 * @version 1.0
 */
public interface AppCardsRepository extends JpaRepository<AppCard, Long> {
    /**
     * Найти карточку по вопросу или ответу.
     *
     * @param question вопрос
     * @param answer   ответ
     * @return найденная карточка или null, если не найдена
     */
    AppCard findByQuestionOrAnswer(String question, String answer);

    /**
     * Найти карточку по ответу.
     *
     * @param answer ответ
     * @return найденная карточка или null, если не найдена
     */
    AppCard findByAnswer(String answer);

    /**
     * Найти карточку по вопросу.
     *
     * @param question вопрос
     * @return найденная карточка или null, если не найдена
     */
    AppCard findByQuestion(String question);

    /**
     * Найти карточки по категории.
     *
     * @param category категория
     * @return список карточек, соответствующих категории
     */
    List<AppCard> findByCategory(CardCategory category);

    /**
     * Найти карточки по списку вопросов.
     *
     * @param questions список вопросов
     * @return список карточек, соответствующих вопросам из списка
     */
    List<AppCard> findByQuestionIn(List<String> questions);
}
