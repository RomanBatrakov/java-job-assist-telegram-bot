package com.batrakov.service.impl;

import com.batrakov.dto.MailParams;
import com.batrakov.entity.AppUser;
import com.batrakov.service.AppUserService;
import com.batrakov.service.RegisterService;
import com.batrakov.service.rabbitService.ProducerService;
import com.batrakov.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static com.batrakov.enums.UserState.BASIC_STATE;
import static com.batrakov.enums.UserState.WAIT_FOR_EMAIL_STATE;

/**
 * Реализация сервиса регистрации пользователя.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements RegisterService {
    @Value("${service.mail.uri}")
    private String mailServiceUri;
    private final AppUserService appUserService;
    private final CryptoTool cryptoTool;
    private final ProducerService producerService;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "`Ты уже зарегистрирован!`";
        } else if (appUser.getEmail() != null) {
            return "`На почту уже было отправлено письмо. `" +
                    "`Перейди по ссылке в письме для подтверждения регистрации.`";
        }
        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserService.updateAppUser(appUser);
        return "`Введи, пожалуйста, email:`";
    }

    @Override
    public void setEmail(AppUser appUser, String email, Long chatId) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            log.error(String.format("Incorrect email=%s for id=%d", email, chatId));
            producerService.sendAnswer("`Введи, пожалуйста, корректный email. Для отмены команды введи` /cancel",
                    chatId);
            return;
        }
        var optionalAppUser = appUserService.findByEmail(email);
        if (optionalAppUser.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUser = appUserService.updateAppUser(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                var msg = String.format("`Отправка эл. письма на почту %s не удалась.`", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserService.updateAppUser(appUser);
                producerService.sendAnswer(msg, chatId);
            }
            producerService.sendAnswer(
                    "`На почту было отправлено письмо. Перейди по ссылке в письме для подтверждения регистрации.`",
                    chatId);
        } else {
            producerService.sendAnswer(
                    "`Этот email уже используется. Введи корректный email. Для отмены команды введи` /cancel", chatId);
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        try {
            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var mailParams = MailParams.builder().id(cryptoUserId).emailTo(email).build();
            var request = new HttpEntity<>(mailParams, headers);
            return restTemplate.exchange(mailServiceUri, HttpMethod.POST, request, String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
