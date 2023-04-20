package com.batrakov.service;

import com.batrakov.entity.AppUser;
import com.batrakov.enums.QuestionState;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Интерфейс сервиса тренировки (TrainingService).
 *
 * @version 1.0
 */
public interface TrainingService {
    void startTraining(Long chatId, AppUser appUser);

    void processTrainingCommand(AppUser appUser, String messageText, Long chatId);

    void processCallBackQuery(CallbackQuery callbackQuery, String callbackData, Long chatId);

    void setQuestionState(CallbackQuery callbackQuery, QuestionState questionState);
}
