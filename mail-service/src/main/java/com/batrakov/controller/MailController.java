package com.batrakov.controller;

import com.batrakov.dto.MailParams;
import com.batrakov.service.MailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки REST-запросов, связанных с отправкой электронной почты.
 *
 * @version 1.0
 */
@AllArgsConstructor
@RequestMapping("/mail")
@RestController
public class MailController {
    private final MailSenderService mailSenderService;

    /**
     * Обработчик POST-запроса для отправки письма активации.
     *
     * @param mailParams объект {@link MailParams} с параметрами письма
     * @return ответ сервера с кодом 200 OK
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams) {
        mailSenderService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
