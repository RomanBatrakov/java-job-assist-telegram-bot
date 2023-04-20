package com.batrakov.service;

import com.batrakov.dto.MailParams;

/**
 * Сервис отправки электронной почты.
 *
 * @version 1.0
 */
public interface MailSenderService {
    /**
     * Отправляет письмо с указанными параметрами.
     *
     * @param mailParams объект {@link MailParams} с параметрами письма
     */
    void send(MailParams mailParams);
}
