package com.batrakov.controller;

import com.batrakov.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Контроллер файлов.
 * Обрабатывает GET-запросы на получение документов и фотографий.
 *
 * @version 1.0
 */
@Slf4j
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * Обрабатывает GET-запрос на получение документа.
     * Если документ с указанным идентификатором не найден, возвращает HTTP-ответ 400 Bad Request.
     * Если документ найден, отправляет его бинарное содержимое в HTTP-ответе с соответствующими заголовками.
     *
     * @param id       Идентификатор документа.
     * @param response HTTP-ответ, в который будет отправлено содержимое документа.
     */
    @GetMapping("/get-doc")
    public void getDoc(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        var doc = fileService.getDocument(id);
        if (doc == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Запрашиваемый документ не найден в базе.");
            return;
        }
        response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + doc.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = doc.getBinaryContent();
        try (var out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обрабатывает GET-запрос на получение фотографии.
     * Если фотография с указанным идентификатором не найдена, возвращает HTTP-ответ 400 Bad Request.
     * Если фотография найдена, отправляет ее бинарное содержимое в HTTP-ответе с соответствующими заголовками.
     *
     * @param id       Идентификатор фотографии.
     * @param response HTTP-ответ, в который будет отправлено содержимое фотографии.
     */
    @GetMapping("/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        var photo = fileService.getPhoto(id);
        if (photo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Запрашиваемое фото не найдено в базе.");
            return;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = photo.getBinaryContent();
        try (var out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
