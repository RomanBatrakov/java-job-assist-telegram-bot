package com.batrakov.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.batrakov.model.RabbitQueue.ANSWER_MESSAGE;
import static com.batrakov.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static com.batrakov.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static com.batrakov.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

/**
 * Конфигурационный класс для настройки очередей RabbitMQ.
 *
 * @version 1.0
 */
@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue docMessageQueue() {
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    @Bean
    public Queue photoMessageQueue() {
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE);
    }
}
