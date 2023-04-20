package com.batrakov.model;

/**
 * Класс, содержащий константы имен очередей RabbitMQ.
 * Определяет имена очередей для различных типов сообщений.
 *
 * @version 1.0
 */
public class RabbitQueue {
    public static final String DOC_MESSAGE_UPDATE = "doc_message_update";
    public static final String PHOTO_MESSAGE_UPDATE = "photo_message_update";
    public static final String TEXT_MESSAGE_UPDATE = "text_message_update";
    public static final String ANSWER_MESSAGE = "answer_message";
}
