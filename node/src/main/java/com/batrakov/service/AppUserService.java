package com.batrakov.service;

import com.batrakov.entity.AppUser;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * Интерфейс сервиса пользователей приложения (AppUser).
 *
 * @version 1.0
 */
public interface AppUserService {
    AppUser findOrCreateAppUser(Update update);

    AppUser updateAppUser(AppUser appUser);

    AppUser findAppUserById(Long chatId);

    Optional<AppUser> findByEmail(String email);

    void deleteAppUserById(Long userId);
}
