package com.batrakov.service.impl;

import com.batrakov.controller.UpdateProcessor;
import com.batrakov.service.AnswerConsumer;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.batrakov.model.RabbitQueue.ANSWER_MESSAGE;

/**
 * Реализация интерфейса AnswerConsumer, представляющая методы для потребления ответных сообщений из RabbitMQ очереди.
 *
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public <T extends BotApiMethod<?>> void consume(T sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
