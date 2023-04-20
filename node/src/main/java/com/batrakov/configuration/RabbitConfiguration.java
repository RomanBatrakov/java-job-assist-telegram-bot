package com.batrakov.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ.
 * Класс содержит настройки конвертера сообщений для работы с RabbitMQ,
 * включая создание бина Jackson2JsonMessageConverter.
 *
 * @version 1.0
 */
@Configuration
public class RabbitConfiguration {
    /**
     * Создание бина Jackson2JsonMessageConverter.
     * Метод создает бин Jackson2JsonMessageConverter, который используется для
     * сериализации и десериализации сообщений в формате JSON при работе с RabbitMQ.
     *
     * @return Объект Jackson2JsonMessageConverter.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
