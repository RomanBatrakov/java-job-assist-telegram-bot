package com.batrakov.service;

import com.batrakov.entity.AppUser;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс главного сервиса (MainService) для обработки текстовых сообщений, документов и фотографий.
 *
 * @version 1.0
 */
public interface MainService {
    void processTextMessage(Update update);

    void processDocMessage(Update update);

    void processPhotoMessage(Update update);

    String cancelProcess(AppUser appUser);
}
