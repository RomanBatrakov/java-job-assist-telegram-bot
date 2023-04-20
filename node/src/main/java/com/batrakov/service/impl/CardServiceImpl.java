package com.batrakov.service.impl;

import com.batrakov.dao.AppCardsRepository;
import com.batrakov.entity.AppCard;
import com.batrakov.enums.CardCategory;
import com.batrakov.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.batrakov.enums.CardCategory.ALL_CATEGORY;

/**
 * Сервис, предоставляющий функционал для работы с карточками приложения.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {
    private final AppCardsRepository appCardsRepository;

    /**
     * Получает случайную карточку определенной категории или из всех категорий.
     *
     * @param cardCategory категория карточки или ALL_CATEGORY для получения из всех категорий
     * @return Optional с объектом AppCard, если найдена карточка, или Optional.empty(), если не найдена
     */
    @Override
    public Optional<AppCard> getRandomCard(CardCategory cardCategory) {
        Random random = new Random();
        List<AppCard> appCardList = ALL_CATEGORY.equals(cardCategory) ? appCardsRepository.findAll() :
                appCardsRepository.findByCategory(cardCategory);
        if (!appCardList.isEmpty()) {
            int index = random.nextInt(appCardList.size());
            return Optional.ofNullable(appCardList.get(index));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AppCard> getCardByQuestion(String question) {
        return Optional.ofNullable(appCardsRepository.findByQuestion(question));
    }

    @Override
    public Optional<AppCard> getCardByAnswer(String answer) {
        return Optional.ofNullable(appCardsRepository.findByAnswer(answer));
    }

    @Override
    public CardCategory getCardByText(String messageText) {
        try {
            return appCardsRepository.findByQuestionOrAnswer(messageText, messageText).getCategory();
        } catch (Exception e) {
            return ALL_CATEGORY;
        }
    }
}
