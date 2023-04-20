package com.batrakov.service.rabbitService;

/**
 * Интерфейс сервиса-производителя для отправки ответов на сообщения.
 *
 * @version 1.0
 */
public interface ProducerService {
    <T> void producerAnswer(T sendMessage);

    void sendAnswer(String output, Long chatId);

    void sendAnswer(String output, Long chatId, Integer messageId);
}
