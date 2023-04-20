package com.batrakov.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Утилитарный класс для генерации объекта {@link SendMessage} с текстом для отправки в Telegram чат.
 *
 * @version 1.0
 */
@Component
public class MessageUtils {

    /**
     * Генерирует объект {@link SendMessage} с указанным текстом для отправки в Telegram чат на основе объекта
     * {@link Update}.
     *
     * @param update объект {@link Update} из Telegram API
     * @param text   текст сообщения для отправки
     * @return объект {@link SendMessage} с настроенными параметрами
     */
    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        return SendMessage.builder()
                          .chatId(message.getChatId().toString())
                          .text(text)
                          .parseMode(ParseMode.MARKDOWN)
                          .build();
    }
}
