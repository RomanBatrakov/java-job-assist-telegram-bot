package com.batrakov.service;

import com.batrakov.entity.AppDocument;
import com.batrakov.entity.AppPhoto;

/**
 * Интерфейс для работы с файлами, предоставляющий методы для получения документов и фотографий.
 *
 * @version 1.0
 */
public interface FileService {
    /**
     * Получить документ по его идентификатору.
     *
     * @param id Идентификатор документа
     * @return Объект AppDocument, представляющий документ
     */
    AppDocument getDocument(String id);

    /**
     * Получить фотографию по ее идентификатору.
     *
     * @param id Идентификатор фотографии
     * @return Объект AppPhoto, представляющий фотографию
     */
    AppPhoto getPhoto(String id);
}
