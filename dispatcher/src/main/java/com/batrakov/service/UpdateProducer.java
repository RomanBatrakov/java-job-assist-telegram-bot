package com.batrakov.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс, представляющий продюсера для отправки обновлений в RabbitMQ очередь.
 *
 * @version 1.0
 */
public interface UpdateProducer {
    /**
     * Метод для отправки обновления в указанную RabbitMQ очередь.
     *
     * @param rabbitQueue Имя RabbitMQ очереди, в которую отправляется обновление.
     * @param update      Объект Update, содержащий информацию об обновлении.
     */
    void produce(String rabbitQueue, Update update);
}
