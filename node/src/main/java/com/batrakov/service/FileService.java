package com.batrakov.service;

import com.batrakov.entity.AppDocument;
import com.batrakov.entity.AppPhoto;
import com.batrakov.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс сервиса работы с файлами (документами и фотографиями).
 *
 * @version 1.0
 */
public interface FileService {
    AppDocument processDoc(Message telegramMessage);

    AppPhoto processPhoto(Message telegramMessage);

    String generateLink(Long docId, LinkType linkType);
}
