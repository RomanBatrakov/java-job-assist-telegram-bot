package com.batrakov.service.impl;

import com.batrakov.dao.AppUserVacancyRepository;
import com.batrakov.entity.AppUser;
import com.batrakov.entity.AppUserVacancy;
import com.batrakov.entity.Vacancy;
import com.batrakov.enums.VacancyState;
import com.batrakov.model.Command;
import com.batrakov.service.AppUserService;
import com.batrakov.service.VacancyService;
import com.batrakov.service.jobSiteService.JobSite;
import com.batrakov.service.jobSiteService.JobSiteFactory;
import com.batrakov.service.rabbitService.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.batrakov.enums.UserState.VACANCIES_STATE;
import static com.batrakov.enums.VacancyState.HIDED;
import static com.batrakov.enums.VacancyState.IN_FAVORITES;
import static com.batrakov.model.Button.FAVOURITE_BUTTON;
import static com.batrakov.model.Button.HIDE_BUTTON;
import static com.batrakov.model.Message.EXCLUDE_WORDS;

/**
 * Это реализация сервиса для управления вакансиями.
 * Он предоставляет методы для получения вакансий для определенного пользователя приложения.
 * Эта реализация использует фабрику сайтов вакансий для извлечения вакансий
 * с различных сайтов вакансий, и сервис-продюсер для отправки уведомлений
 * о новых вакансиях пользователю приложения.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VacancyServiceImpl implements VacancyService {
    private final AppUserService appUserService;
    private final JobSiteFactory jobSiteFactory;
    private final ProducerService producerService;
    private final AppUserVacancyRepository appUserVacancyRepository;

    @Override
    public void getVacancies(AppUser appUser, Long chatId) {
        appUser.setState(VACANCIES_STATE);
        appUserService.updateAppUser(appUser);

        JobSite headhunter = jobSiteFactory.createJobSite("headhunter");
        JobSite habr = jobSiteFactory.createJobSite("habr");
        JobSite proglib = jobSiteFactory.createJobSite("proglib");

        List<Vacancy> mergedVacancies = Stream.of(headhunter, habr, proglib)
                                              .parallel()
                                              .map(JobSite::getVacancies)
                                              .flatMap(List::stream)
                                              .collect(Collectors.toList());

        List<Vacancy> filteredVacancies = vacanciesFilter(mergedVacancies);
        List<Vacancy> desiredVacancies = hidedVacanciesFilter(filteredVacancies, appUser);

        for (Vacancy vacancy : desiredVacancies) {
            SendMessage message = SendMessage.builder()
                                             .chatId(chatId)
                                             .replyMarkup(setVacancyButton())
                                             .parseMode(ParseMode.MARKDOWN)
                                             .text(vacancy.toString())
                                             .build();
            producerService.producerAnswer(message);
        }
    }

    @Override
    public void processVacancyCommand(AppUser appUser, String messageText, Long chatId) {
        String text = "`В режиме вакансий такой команды нет. Для выхода в основной режим нажми` /cancel";
        var serviceCommand = Command.parseCommand(messageText);
        if (serviceCommand.isPresent()) {
            switch (serviceCommand.get()) {
                case VACANCIES -> getVacancies(appUser, chatId);
                case FAVOURITES -> getFavouritesVacancies(appUser, chatId);
                default -> producerService.sendAnswer(text, chatId);
            }
        } else {
            producerService.sendAnswer(text, chatId);
        }
    }

    @Override
    public void processCallBackQuery(CallbackQuery callbackQuery, String callbackData, Long chatId) {
        switch (callbackData) {
            case "Favourite" -> setVacancyState(callbackQuery, IN_FAVORITES);
            case "Hide" -> setVacancyState(callbackQuery, HIDED);
        }
    }

    @Override
    public void getFavouritesVacancies(AppUser appUser, Long chatId) {
        appUser.setState(VACANCIES_STATE);
        appUserService.updateAppUser(appUser);
        List<String> favoritesVacancies = appUserVacancyRepository.findByAppUserAndVacancyState(appUser, IN_FAVORITES)
                                                                  .stream()
                                                                  .map(AppUserVacancy::getUrl)
                                                                  .toList();

        if (favoritesVacancies.isEmpty()) {
            String text = "`Пока что тебе ничего не приглянулось.`";
            producerService.sendAnswer(text, chatId);
        } else {
            for (String url : favoritesVacancies) {
                SendMessage message = SendMessage.builder()
                                                 .chatId(chatId)
                                                 .parseMode(ParseMode.MARKDOWN)
                                                 .replyMarkup(setVacancyButton())
                                                 .text(url)
                                                 .build();
                producerService.producerAnswer(message);
            }
        }
    }

    @Override
    public void setVacancyState(CallbackQuery callbackQuery, VacancyState vacancyState) {
        Long chatId = callbackQuery.getMessage().getChatId();
        AppUser appUser = appUserService.findAppUserById(chatId);
        String messageText = callbackQuery.getMessage().getText();

        String url = Arrays.stream(messageText.split("\\n"))
                           .skip(1)
                           .filter(s -> s.startsWith("http"))
                           .findFirst()
                           .orElse("wrong url");

        Optional<AppUserVacancy> appUserVacancy = appUserVacancyRepository.findByUrlAndAppUser(url, appUser);
        String text = "Статус вакансии обновлён.";

        if (appUserVacancy.isPresent()) {
            AppUserVacancy existVacancy = appUserVacancy.get();
            if (existVacancy.getVacancyState().equals(vacancyState)) {
                appUserVacancyRepository.delete(existVacancy);
            } else {
                existVacancy.setVacancyState(vacancyState);
                appUserVacancyRepository.save(existVacancy);
            }
        } else {
            AppUserVacancy vacancy =
                    AppUserVacancy.builder().appUser(appUser).vacancyState(vacancyState).url(url).build();
            appUserVacancyRepository.save(vacancy);
        }

        AnswerCallbackQuery build = AnswerCallbackQuery.builder()
                                                       .cacheTime(2)
                                                       .showAlert(false)
                                                       .text(text)
                                                       .callbackQueryId(callbackQuery.getId())
                                                       .build();
        producerService.producerAnswer(build);
    }

    private List<Vacancy> vacanciesFilter(List<Vacancy> vacancies) {
        return vacancies.stream()
                        .filter(vacancy -> EXCLUDE_WORDS.stream().noneMatch(vacancy.getName().toLowerCase()::contains))
                        .collect(Collectors.toList());
    }

    private List<Vacancy> hidedVacanciesFilter(List<Vacancy> filteredVacancies, AppUser appUser) {
        List<AppUserVacancy> userVacancies = appUser.getUserVacancies()
                                                    .stream()
                                                    .filter(appUserVacancy -> appUserVacancy.getVacancyState()
                                                                                            .equals(VacancyState.HIDED))
                                                    .toList();
        return filteredVacancies.stream()
                                .filter(vacancy -> userVacancies.stream()
                                                                .noneMatch(userVacancy -> userVacancy.getUrl()
                                                                                                     .equals(vacancy.getUrl())))
                                .collect(Collectors.toList());
    }

    private InlineKeyboardMarkup setVacancyButton() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(Arrays.asList(InlineKeyboardButton.builder()
                                                         .text(FAVOURITE_BUTTON.getName())
                                                         .callbackData(FAVOURITE_BUTTON.getDescription())
                                                         .build(), InlineKeyboardButton.builder()
                                                                                       .text(HIDE_BUTTON.getName())
                                                                                       .callbackData(
                                                                                               HIDE_BUTTON.getDescription())
                                                                                       .build()));
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }
}
