package com.batrakov.controller;

import com.batrakov.service.UserActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер активации пользователя.
 * Обрабатывает GET-запрос на активацию пользователя.
 *
 * @version 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class ActivationController {
    private final UserActivationService userActivationService;

    /**
     * Обрабатывает GET-запрос на активацию пользователя.
     * Если активация прошла успешно, возвращает HTTP-ответ 200 OK с сообщением "Регистрация успешно завершена!".
     * Если активация не удалась, возвращает HTTP-ответ 500 Internal Server Error.
     *
     * @param id Идентификатор пользователя для активации.
     * @return HTTP-ответ с результатом активации пользователя.
     */
    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        var res = userActivationService.activation(id);
        if (res) {
            return ResponseEntity.ok().body("Регистрация успешно завершена!");
        }
        return ResponseEntity.internalServerError().build();
    }
}
