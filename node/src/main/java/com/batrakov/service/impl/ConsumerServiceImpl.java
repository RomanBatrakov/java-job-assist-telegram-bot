package com.batrakov.service.impl;

import com.batrakov.service.MainService;
import com.batrakov.service.rabbitService.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.batrakov.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static com.batrakov.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static com.batrakov.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

/**
 * Сервис, слушающий и обрабатывающий сообщения из RabbitMQ.
 *
 * @version 1.0
 */
@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");
        mainService.processPhotoMessage(update);
    }
}
