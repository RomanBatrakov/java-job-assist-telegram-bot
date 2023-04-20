package com.batrakov.service.impl;

import com.batrakov.dao.AppUserQuestionRepository;
import com.batrakov.entity.AppCard;
import com.batrakov.entity.AppUser;
import com.batrakov.entity.AppUserQuestion;
import com.batrakov.enums.CardCategory;
import com.batrakov.enums.QuestionState;
import com.batrakov.model.Command;
import com.batrakov.service.AppUserService;
import com.batrakov.service.CardService;
import com.batrakov.service.TrainingService;
import com.batrakov.service.rabbitService.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.batrakov.enums.CardCategory.ALL_CATEGORY;
import static com.batrakov.enums.CardCategory.HIBERNATE;
import static com.batrakov.enums.CardCategory.JAVA_COLLECTION;
import static com.batrakov.enums.CardCategory.JAVA_CORE;
import static com.batrakov.enums.CardCategory.MULTITHREADING;
import static com.batrakov.enums.CardCategory.OTHER;
import static com.batrakov.enums.CardCategory.SPRING;
import static com.batrakov.enums.CardCategory.SQL;
import static com.batrakov.enums.QuestionState.HIDED;
import static com.batrakov.enums.UserState.TRAINING_STATE;
import static com.batrakov.model.Button.ANSWER_BUTTON;
import static com.batrakov.model.Button.HIDE_QUESTION_BUTTON;
import static com.batrakov.model.Button.NEXT_BUTTON;
import static com.batrakov.model.Button.PREVIOUS_BUTTON;
import static com.batrakov.model.Button.QUESTION_BUTTON;
import static com.batrakov.model.Command.TRAINING;
import static com.batrakov.model.Message.QUESTION_NOT_DOWNLOAD_TEXT;
import static com.batrakov.model.Message.WRONG_TRAINING_COMMAND_TEXT;

/**
 * Реализация сервиса для изучения/повторения теории при подготовке к собеседованию.
 * Класс предоставляет методы для начала обучения, обработки команд обучения, обработки
 * ответов на вопросы, и генерации карточек с вопросами для обучения пользователя.
 *
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {
    private final AppUserService appUserService;
    private final ProducerService producerService;
    private final CardService cardService;
    private final AppUserQuestionRepository appUserQuestionRepository;

    @Override
    public void startTraining(Long chatId, AppUser appUser) {
        appUser.setState(TRAINING_STATE);
        appUserService.updateAppUser(appUser);

        String text = "`Какую тему хочешь прокачать?`";

        InlineKeyboardMarkup markupInLine = setCardCategoryButtons();

        SendMessage message = SendMessage.builder()
                                         .chatId(chatId)
                                         .parseMode(ParseMode.MARKDOWN)
                                         .text(text)
                                         .replyMarkup(markupInLine)
                                         .build();

        producerService.producerAnswer(message);
    }

    @Override
    public void processTrainingCommand(AppUser appUser, String messageText, Long chatId) {
        var serviceCommand = Command.parseCommand(messageText);
        if (serviceCommand.isPresent() && TRAINING.equals(serviceCommand.get())) {
            startTraining(chatId, appUser);
        } else {
            producerService.sendAnswer(WRONG_TRAINING_COMMAND_TEXT, chatId);
        }
    }

    @Override
    public void processCallBackQuery(CallbackQuery callbackQuery, String callbackData, Long chatId) {
        switch (callbackData) {
            case "Ответ" -> getCardByText(callbackQuery, false);
            case "Вопрос" -> getCardByText(callbackQuery, true);
            case "Previous", "Next" -> getNextCard(callbackQuery);
            case "Скрыть" -> setQuestionState(callbackQuery, HIDED);
            default -> generateCards(callbackData, chatId);
        }
    }

    public void generateCards(String callbackData, Long chatId) {
        Optional<CardCategory> cardCategory = CardCategory.parseCardCategory(callbackData);

        if (cardCategory.isPresent()) {
            CardCategory category = cardCategory.get();
            Optional<AppCard> appCard = cardService.getRandomCard(category);
            if (appCard.isPresent()) {
                sendRandomCardQuestion(chatId, appCard);
            } else {
                producerService.sendAnswer(QUESTION_NOT_DOWNLOAD_TEXT, chatId);
            }
        } else {
            producerService.sendAnswer("`Неизвестная категория вопросов.`", chatId);
        }
    }

    private void sendRandomCardQuestion(Long chatId, Optional<AppCard> appCard) {
        String text = QUESTION_NOT_DOWNLOAD_TEXT;
        if (appCard.isPresent()) text = appCard.get().getQuestion();

        SendMessage message = SendMessage.builder()
                                         .chatId(chatId)
                                         .parseMode(ParseMode.MARKDOWN)
                                         .text(text)
                                         .replyMarkup(setCardNavigateButton(true))
                                         .build();
        producerService.producerAnswer(message);
    }

    private void getCardByText(CallbackQuery callbackQuery, boolean isQuestion) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String messageText = callbackQuery.getMessage().getText();
        Optional<AppCard> appCard =
                isQuestion ? cardService.getCardByAnswer(messageText) : cardService.getCardByQuestion(messageText);

        String text = "`Не могу в памяти найти ничего, попробуй другой вопрос.`";
        if (appCard.isPresent()) {
            text = isQuestion ? appCard.get().getQuestion() : appCard.get().getAnswer();
        }

        EditMessageText message = EditMessageText.builder()
                                                 .chatId(chatId)
                                                 .messageId(messageId)
                                                 .replyMarkup(setCardNavigateButton(isQuestion))
                                                 .parseMode(ParseMode.MARKDOWN)
                                                 .text(text)
                                                 .build();
        producerService.producerAnswer(message);
    }

    private void getNextCard(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String messageText = callbackQuery.getMessage().getText();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        CardCategory category = cardService.getCardByText(messageText);
        Optional<AppCard> appCard = cardService.getRandomCard(category);
        String text = QUESTION_NOT_DOWNLOAD_TEXT;
        if (appCard.isPresent()) text = appCard.get().getQuestion();

        EditMessageText message = EditMessageText.builder()
                                                 .chatId(chatId)
                                                 .messageId(messageId)
                                                 .replyMarkup(setCardNavigateButton(true))
                                                 .parseMode(ParseMode.MARKDOWN)
                                                 .text(text)
                                                 .build();
        producerService.producerAnswer(message);
    }

    @Override
    public void setQuestionState(CallbackQuery callbackQuery, QuestionState questionState) {
        Long chatId = callbackQuery.getMessage().getChatId();
        AppUser appUser = appUserService.findAppUserById(chatId);
        String messageText = callbackQuery.getMessage().getText();

        Optional<AppUserQuestion> appUserQuestion =
                appUserQuestionRepository.findByQuestionAndAppUser(messageText, appUser);
        String text = "Статус вопроса обновлён.";

        if (appUserQuestion.isPresent()) {
            AppUserQuestion existQuestion = appUserQuestion.get();
            if (existQuestion.getQuestionState().equals(questionState)) {
                appUserQuestionRepository.delete(existQuestion);
            } else {
                existQuestion.setQuestionState(questionState);
                appUserQuestionRepository.save(existQuestion);
            }
        } else {
            AppUserQuestion question = AppUserQuestion.builder()
                                                      .appUser(appUser)
                                                      .questionState(questionState)
                                                      .question(messageText)
                                                      .build();
            appUserQuestionRepository.save(question);
        }

        AnswerCallbackQuery build = AnswerCallbackQuery.builder()
                                                       .cacheTime(2)
                                                       .showAlert(false)
                                                       .text(text)
                                                       .callbackQueryId(callbackQuery.getId())
                                                       .build();
        producerService.producerAnswer(build);
    }

    private InlineKeyboardMarkup setCardCategoryButtons() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(Arrays.asList(InlineKeyboardButton.builder()
                                                         .text(ALL_CATEGORY.getName())
                                                         .callbackData(ALL_CATEGORY.getDescription())
                                                         .build(), InlineKeyboardButton.builder()
                                                                                       .text(JAVA_CORE.getName())
                                                                                       .callbackData(
                                                                                               JAVA_CORE.getDescription())
                                                                                       .build()));
        rowsInLine.add(Arrays.asList(InlineKeyboardButton.builder()
                                                         .text(JAVA_COLLECTION.getName())
                                                         .callbackData(JAVA_COLLECTION.getDescription())
                                                         .build(),
                InlineKeyboardButton.builder().text(SPRING.getName()).callbackData(SPRING.getDescription()).build()));
        rowsInLine.add(Arrays.asList(
                InlineKeyboardButton.builder().text(SQL.getName()).callbackData(SQL.getDescription()).build(),
                InlineKeyboardButton.builder()
                                    .text(HIBERNATE.getName())
                                    .callbackData(HIBERNATE.getDescription())
                                    .build()));
        rowsInLine.add(Arrays.asList(InlineKeyboardButton.builder()
                                                         .text(MULTITHREADING.getName())
                                                         .callbackData(MULTITHREADING.getDescription())
                                                         .build(),
                InlineKeyboardButton.builder().text(OTHER.getName()).callbackData(OTHER.getDescription()).build()));
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private InlineKeyboardMarkup setCardNavigateButton(boolean isQuestion) {
        String buttonText = isQuestion ? ANSWER_BUTTON.getDescription() : QUESTION_BUTTON.getDescription();
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(Arrays.asList(InlineKeyboardButton.builder()
                                                         .text(PREVIOUS_BUTTON.getName())
                                                         .callbackData(PREVIOUS_BUTTON.getDescription())
                                                         .build(),
                InlineKeyboardButton.builder().text(buttonText).callbackData(buttonText).build(),
                InlineKeyboardButton.builder()
                                    .text(NEXT_BUTTON.getName())
                                    .callbackData(NEXT_BUTTON.getDescription())
                                    .build(), InlineKeyboardButton.builder()
                                                                  .text(HIDE_QUESTION_BUTTON.getName())
                                                                  .callbackData(HIDE_QUESTION_BUTTON.getDescription())
                                                                  .build()));
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }
}
