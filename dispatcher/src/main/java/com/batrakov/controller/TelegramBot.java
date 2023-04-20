package com.batrakov.controller;

import com.batrakov.model.Command;
import com.batrakov.service.CardService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

/**
 * Класс Telegram-бота, расширяющий TelegramLongPollingBot.
 *
 * @version 1.0
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${JJAB_USERNAME}")
    private String botName;
    private final CardService cardService;
    private final UpdateProcessor updateProcessor;

    public TelegramBot(@Value("${JJAB_TOKEN}") String botToken, CardService cardService,
                       UpdateProcessor updateProcessor) {
        super(botToken);
        this.cardService = cardService;
        this.updateProcessor = updateProcessor;
    }

    /**
     * Метод-обработчик события инициализации бота.
     * Выполняет настройку команд бота и загрузку карточек из файла.
     */
    @PostConstruct
    public void init() {
        setupCommands();
        String fileName = "/common-jpa/src/main/resources/cards.json";
        cardService.loadCards(fileName);
    }

    private void setupCommands() {
        try {
            List<BotCommand> commands = Arrays.stream(Command.values())
                                              .map(c -> BotCommand.builder()
                                                                  .command(c.getName())
                                                                  .description(c.getDescription())
                                                                  .build())
                                              .toList();
            execute(SetMyCommands.builder().commands(commands).build());
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    /**
     * Отправляет ответное сообщение от бота.
     *
     * @param message объект сообщения для отправки
     */
    public void sendAnswerMessage(BotApiMethod<?> message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error executing message: " + e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateProcessor.processUpdate(update);
    }
}
