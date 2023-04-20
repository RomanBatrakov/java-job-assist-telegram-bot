package com.batrakov.service.rabbitService;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс сервиса-потребителя для обработки обновлений текстовых сообщений, документов и фотографий.
 *
 * @version 1.0
 */
public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);

    void consumeDocMessageUpdates(Update update);

    void consumePhotoMessageUpdates(Update update);
}
