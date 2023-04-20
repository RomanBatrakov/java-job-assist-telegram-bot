package com.batrakov.service;

import com.batrakov.entity.AppCard;
import com.batrakov.enums.CardCategory;

import java.util.Optional;

/**
 * Интерфейс сервиса карточек (AppCard).
 *
 * @version 1.0
 */
public interface CardService {
    Optional<AppCard> getRandomCard(CardCategory javaCore);

    Optional<AppCard> getCardByQuestion(String question);

    Optional<AppCard> getCardByAnswer(String answer);

    CardCategory getCardByText(String messageText);
}
