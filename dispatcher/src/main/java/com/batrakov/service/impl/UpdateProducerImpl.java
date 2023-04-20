package com.batrakov.service.impl;

import com.batrakov.service.UpdateProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Реализация интерфейса {@link UpdateProducer} для отправки объекта {@link Update} в RabbitMQ очередь.
 *
 * @version 1.0
 */
@Slf4j
@AllArgsConstructor
@Service
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    /**
     * Отправляет объект {@link Update} в указанную RabbitMQ очередь.
     *
     * @param rabbitQueue имя RabbitMQ очереди
     * @param update      объект {@link Update} для отправки
     */
    @Override
    public void produce(String rabbitQueue, Update update) {
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
