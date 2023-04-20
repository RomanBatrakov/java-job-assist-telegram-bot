package com.batrakov.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс-инициализатор бота.
 * Регистрирует бота при событии запуска контекста приложения.
 *
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class BotInitializer {
    TelegramBot bot;

    /**
     * Метод-обработчик события запуска контекста приложения.
     * Регистрирует бота при запуске контекста приложения.
     *
     * @throws TelegramApiException в случае ошибки при регистрации бота
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error to register bot: " + e.getMessage());
        }
    }
}
