package com.batrakov.service;

import com.batrakov.entity.AppUser;

/**
 * Интерфейс сервиса регистрации пользователей (RegisterService).
 *
 * @version 1.0
 */
public interface RegisterService {
    String registerUser(AppUser appUser);

    void setEmail(AppUser appUser, String email, Long chatId);
}
