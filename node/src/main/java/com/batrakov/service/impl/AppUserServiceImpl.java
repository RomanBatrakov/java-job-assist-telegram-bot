package com.batrakov.service.impl;

import com.batrakov.dao.AppUserRepository;
import com.batrakov.entity.AppUser;
import com.batrakov.service.AppUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Optional;

import static com.batrakov.enums.UserState.BASIC_STATE;

/**
 * Сервис, предоставляющий функционал для работы с пользователями приложения.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Override
    public AppUser findOrCreateAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        var optional = appUserRepository.findByTelegramUserId(telegramUser.getId());
        if (optional.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                                              .telegramUserId(telegramUser.getId())
                                              .username(telegramUser.getUserName())
                                              .firstName(telegramUser.getFirstName())
                                              .lastName(telegramUser.getLastName())
                                              .isActive(false)
                                              .state(BASIC_STATE)
                                              .userVacancies(new ArrayList<>())
                                              .build();
            return appUserRepository.save(transientAppUser);
        }
        return optional.get();
    }

    @Override
    public AppUser updateAppUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser findAppUserById(Long chatId) {
        try {
            return appUserRepository.findByTelegramUserId(chatId).get();
        } catch (Exception e) {
            log.error("User is not found");
            return new AppUser();
        }
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void deleteAppUserById(Long userId) {
        try {
            appUserRepository.deleteByTelegramUserId(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
