package com.batrakov.service.impl;

import com.batrakov.entity.AppUser;
import com.batrakov.service.AppUserService;
import com.batrakov.service.ExitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.batrakov.model.Button.NO_EXIT_BUTTON;
import static com.batrakov.model.Button.YES_EXIT_BUTTON;
/**
 * Сервис реализующий выход пользователя из чата.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ExitServiceImpl implements ExitService {
    private final ProducerServiceImpl producerService;
    private final AppUserService appUserService;

    @Override
    public void exit(long chatId, AppUser appUser) {
        appUserService.updateAppUser(appUser);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        rowsInLine.add(Arrays.asList(
                InlineKeyboardButton.builder()
                                    .text(YES_EXIT_BUTTON.getName())
                                    .callbackData(YES_EXIT_BUTTON.getDescription())
                                    .build(),
                InlineKeyboardButton.builder()
                                    .text(NO_EXIT_BUTTON.getName())
                                    .callbackData(NO_EXIT_BUTTON.getDescription())
                                    .build()));
        markupInLine.setKeyboard(rowsInLine);

        SendMessage message = SendMessage.builder()
                                         .chatId(chatId)
                                         .parseMode(ParseMode.MARKDOWN)
                                         .text("`Действиетльно решили уйти?`")
                                         .replyMarkup(markupInLine)
                                         .build();

        producerService.producerAnswer(message);
    }

    @Override
    public void approveExit(Long chatId, Integer messageId) {
        appUserService.deleteAppUserById(chatId);
        String text = "`Рад был пообщаться! Всего доброго. Твои данные удалены.`";
        producerService.sendAnswer(text, chatId, messageId);
    }

    @Override
    public void declineExit(Long chatId, Integer messageId) {
        String text = "`Отлично! Продолжай с того места где остановился.`";
        producerService.sendAnswer(text, chatId, messageId);
    }
}
