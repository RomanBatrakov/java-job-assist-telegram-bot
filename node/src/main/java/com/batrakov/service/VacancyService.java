package com.batrakov.service;

import com.batrakov.entity.AppUser;
import com.batrakov.enums.VacancyState;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Интерфейс сервиса вакансий (VacancyService).
 *
 * @version 1.0
 */
public interface VacancyService {

    void getVacancies(AppUser appUser, Long chatId);

    void processVacancyCommand(AppUser appUser, String messageText, Long chatId);

    void setVacancyState(CallbackQuery callbackQuery, VacancyState vacancyState);

    void processCallBackQuery(CallbackQuery callbackQuery, String callbackData, Long chatId);

    void getFavouritesVacancies(AppUser appUser, Long chatId);
}
