package com.batrakov.service.impl;

import com.batrakov.dto.MailParams;
import com.batrakov.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса MailSenderService для отправки электронных писем.
 * Использует JavaMailSender для отправки писем с использованием настроек
 * из application.properties.
 *
 * @version 1.0
 */
@Slf4j
@Service
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${MAIL_SERVICE_NAME}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUri;

    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Метод для отправки письма с параметрами, указанными в MailParams.
     *
     * @param mailParams Параметры для отправки письма.
     */
    @Override
    public void send(MailParams mailParams) {
        var subject = "Активация учетной записи";
        var messageBody = getActivationMailBody(mailParams.getId());
        var emailTo = mailParams.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error("Ошибка при отправке письма", e);
            throw new RuntimeException("Ошибка при отправке письма", e);
        }
    }

    /**
     * Метод для генерации тела письма активации с использованием ID из MailParams.
     *
     * @param id Идентификатор для подстановки в тело письма.
     * @return Тело письма активации.
     */
    private String getActivationMailBody(String id) {
        var msg = String.format("Для завершения регистрации перейдите по ссылке:\n%s", activationServiceUri);
        return msg.replace("{id}", id);
    }
}
