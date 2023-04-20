package com.batrakov.service.impl;

import com.batrakov.dao.AppCardsRepository;
import com.batrakov.entity.AppCard;
import com.batrakov.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link CardService} для загрузки карточек из файла в базу данных.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {
    private final AppCardsRepository appCardsRepository;

    /**
     * Загружает карточки из файла в базу данных.
     *
     * @param fileName имя файла с карточками
     */
    @Override
    public void loadCards(String fileName) {
        try (FileReader fileReader = new FileReader("/files/cards.json");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            ObjectMapper objectMapper = new ObjectMapper();
            AppCard[] appCards = objectMapper.readValue(bufferedReader, AppCard[].class);

            List<AppCard> existingCards = appCardsRepository.findByQuestionIn(
                    Arrays.stream(appCards).map(AppCard::getQuestion).collect(Collectors.toList()));
            List<AppCard> newCards = Arrays.stream(appCards)
                                           .filter(card -> existingCards.stream()
                                                                        .noneMatch(
                                                                                existingCard -> existingCard.getQuestion()
                                                                                                            .equals(card.getQuestion())))
                                           .collect(Collectors.toList());

            appCardsRepository.saveAll(newCards);
            log.info("Карточки добавлены в базу");
        } catch (IOException e) {
            log.error("Ошибка загрузки карточек в базу: " + e.getMessage());
        }
    }
}
