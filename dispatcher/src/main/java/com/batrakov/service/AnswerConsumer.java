package com.batrakov.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.batrakov.model.RabbitQueue.ANSWER_MESSAGE;

/**
 * Интерфейс, представляющий слушателя RabbitMQ очереди для приема ответных сообщений от бота.
 * Метод consume принимает объект типа T, который должен быть наследником BotApiMethod,
 * и обрабатывает его в соответствии с логикой приложения.
 *<p>
 * <T> Тип объекта, наследника BotApiMethod, который будет обработан при приеме сообщения из очереди.
 *
 * @version 1.0
 */
public interface AnswerConsumer {
    @RabbitListener(queues = ANSWER_MESSAGE)
    <T extends BotApiMethod<?>> void consume(T sendMessage);
}
