package com.batrakov.service.impl;

import com.batrakov.entity.AppDocument;
import com.batrakov.entity.AppPhoto;
import com.batrakov.entity.AppUser;
import com.batrakov.exeptions.UploadFileException;
import com.batrakov.model.Button;
import com.batrakov.model.Command;
import com.batrakov.service.AppUserService;
import com.batrakov.service.ExitService;
import com.batrakov.service.FileService;
import com.batrakov.service.MainService;
import com.batrakov.service.RegisterService;
import com.batrakov.service.TrainingService;
import com.batrakov.service.VacancyService;
import com.batrakov.service.enums.LinkType;
import com.batrakov.service.rabbitService.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.batrakov.enums.UserState.BASIC_STATE;
import static com.batrakov.enums.UserState.TRAINING_STATE;
import static com.batrakov.enums.UserState.VACANCIES_STATE;
import static com.batrakov.model.Command.CANCEL;
import static com.batrakov.model.Command.TRAINING;
import static com.batrakov.model.Command.VACANCIES;
import static com.batrakov.model.Message.FAQ_TEXT;
import static com.batrakov.model.Message.START_TEXT;
import static com.batrakov.model.Message.UNKNOWN_COMMAND_TEXT;
import static com.batrakov.model.Message.WRONG_COMMAND_FOR_THIS_STATE_TEXT;
/**
 * Этот класс реализует интерфейс {@link MainService} и предоставляет
 * основную логику для обработки текстовых, документных и фото-сообщений
 * в боте Telegram. Он обрабатывает различные команды сервиса и состояния
 * пользователя, чтобы направлять сообщения на соответствующие сервисы
 * для дальнейшей обработки.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
    private final ProducerService producerService;
    private final FileService fileService;
    private final RegisterService registerService;
    private final ExitService exitService;
    private final AppUserService appUserService;
    private final TrainingService trainingService;
    private final VacancyService vacancyService;

    @Override
    public void processTextMessage(Update update) {
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        } else {
            var appUser = appUserService.findOrCreateAppUser(update);
            var userState = appUser.getState();
            var messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            var serviceCommand = Command.parseCommand(messageText);
            if (serviceCommand.isPresent() && CANCEL.equals(serviceCommand.get())) {
                String output = cancelProcess(appUser);
                producerService.sendAnswer(output, chatId);
                return;
            }

            switch (userState) {
                case BASIC_STATE -> processServiceCommand(update, messageText);
                case TRAINING_STATE -> trainingService.processTrainingCommand(appUser, messageText, chatId);
                case VACANCIES_STATE -> vacancyService.processVacancyCommand(appUser, messageText, chatId);
                case WAIT_FOR_EMAIL_STATE -> registerService.setEmail(appUser, messageText, chatId);
                default -> {
                    log.error("Unknown user state: " + userState);
                    producerService.sendAnswer("`Неизвестная ошибка! Введи` /cancel `и попробуй снова!`", chatId);
                }
            }

        }
    }

    @Override
    public void processDocMessage(Update update) {
        var appUser = appUserService.findOrCreateAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            var answer = "`Документ успешно загружен! Ссылка для скачивания:` \n" + link;
            producerService.sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(String.valueOf(ex));
            String error = "`Загрузка файла не удалась. Проверь что он не больше 5МБ и повтори попытку позже.`";
            producerService.sendAnswer(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        var appUser = appUserService.findOrCreateAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            var answer = "`Фото успешно загружено! Ссылка для скачивания: `" + link;
            producerService.sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(String.valueOf(ex));
            String error = "`Загрузка фото не удалась. Проверь что оно не больше 5МБ и повтори попытку позже.`";
            producerService.sendAnswer(error, chatId);
        }
    }

    private void processServiceCommand(Update update, String messageText) {
        var appUser = appUserService.findOrCreateAppUser(update);
        var chatId = update.getMessage().getChatId();
        var serviceCommand = Command.parseCommand(messageText);

        if (serviceCommand.isPresent()) {
            switch (serviceCommand.get()) {
                case FAQ -> producerService.sendAnswer(FAQ_TEXT, chatId);
                case START -> setStartKeyboard(appUser, chatId);
                case REGISTRATION -> producerService.sendAnswer(registerService.registerUser(appUser), chatId);
                case VACANCIES -> vacancyService.getVacancies(appUser, chatId);
                case FAVOURITES -> vacancyService.getFavouritesVacancies(appUser, chatId);
                case TRAINING -> trainingService.startTraining(chatId, appUser);
                case CANCEL -> producerService.sendAnswer(cancelProcess(appUser), chatId);
                case EXIT -> exitService.exit(chatId, appUser);
                default -> producerService.sendAnswer(UNKNOWN_COMMAND_TEXT, chatId);
            }
        } else {
            producerService.sendAnswer(UNKNOWN_COMMAND_TEXT, chatId);
        }
    }

    //TODO: переделать условие с serviceButton на callbackData
    private void processCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (!callbackData.isEmpty()) {
            AppUser appUser = appUserService.findAppUserById(chatId);
            if (TRAINING_STATE.equals(appUser.getState())) {
                trainingService.processCallBackQuery(callbackQuery, callbackData, chatId);
            } else if (VACANCIES_STATE.equals(appUser.getState())) {
                vacancyService.processCallBackQuery(callbackQuery, callbackData, chatId);
            } else {
                var serviceButton = Button.parseButton(callbackData);
                if (serviceButton.isPresent()) {
                    switch (serviceButton.get()) {
                        case YES_EXIT_BUTTON -> exitService.approveExit(chatId, messageId);
                        case NO_EXIT_BUTTON -> exitService.declineExit(chatId, messageId);
                        default -> producerService.sendAnswer(UNKNOWN_COMMAND_TEXT, chatId);
                    }
                } else {
                    log.error("Empty serviceButton");
                    producerService.sendAnswer(WRONG_COMMAND_FOR_THIS_STATE_TEXT, chatId);
                }
            }
        } else {
            producerService.sendAnswer(UNKNOWN_COMMAND_TEXT, chatId);
        }
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "`Зарегистрируйся или активируй свою учетную запись для загрузки контента.`";
            producerService.sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "`Отмени текущую команду с помощью` /cancel `для отправки файлов.`";
            producerService.sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserService.updateAppUser(appUser);
        return "`Команда отменена!`";
    }

    private void setStartKeyboard(AppUser appUser, Long chatId) {
        String text = String.format(START_TEXT, appUser.getFirstName());

        ReplyKeyboardMarkup keyboardMarkup =
                ReplyKeyboardMarkup.builder().resizeKeyboard(true).oneTimeKeyboard(false).build();

        KeyboardRow row = new KeyboardRow();
        row.add(VACANCIES.getDescription());
        row.add(TRAINING.getDescription());

        keyboardMarkup.setKeyboard(List.of(row));

        SendMessage sendMessage = SendMessage.builder()
                                             .parseMode(ParseMode.MARKDOWN)
                                             .text(text)
                                             .replyMarkup(keyboardMarkup)
                                             .chatId(chatId)
                                             .build();

        producerService.producerAnswer(sendMessage);
    }
}
