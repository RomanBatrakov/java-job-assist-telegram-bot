package com.batrakov.service.impl;

import com.batrakov.service.rabbitService.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static com.batrakov.model.RabbitQueue.ANSWER_MESSAGE;
/**
 * Этот класс реализует интерфейс {@link ProducerService} и предоставляет
 * реализацию для отправки сообщений в RabbitMQ. Он принимает на вход
 * текстовые сообщения и отправляет их в соответствующую очередь RabbitMQ
 * для дальнейшей обработки другими сервисами.
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public <T> void producerAnswer(T sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }

    @Override
    public void sendAnswer(String output, Long chatId) {
        SendMessage message = SendMessage.builder().chatId(chatId).parseMode(ParseMode.MARKDOWN).text(output).build();
        producerAnswer(message);
    }

    @Override
    public void sendAnswer(String output, Long chatId, Integer messageId) {
        EditMessageText message = EditMessageText.builder()
                                                 .chatId(chatId)
                                                 .messageId(messageId)
                                                 .parseMode(ParseMode.MARKDOWN)
                                                 .text(output)
                                                 .build();
        producerAnswer(message);
    }
}
