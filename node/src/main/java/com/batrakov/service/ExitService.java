package com.batrakov.service;

import com.batrakov.entity.AppUser;

/**
 * Интерфейс сервиса выхода (ExitService).
 *
 * @version 1.0
 */
public interface ExitService {
    void exit(long chatId, AppUser appUser);

    void approveExit(Long chatId, Integer messageId);

    void declineExit(Long chatId, Integer messageId);
}
