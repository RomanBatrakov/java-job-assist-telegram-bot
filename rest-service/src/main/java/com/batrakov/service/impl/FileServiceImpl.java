package com.batrakov.service.impl;

import com.batrakov.dao.AppDocumentRepository;
import com.batrakov.dao.AppPhotoRepository;
import com.batrakov.entity.AppDocument;
import com.batrakov.entity.AppPhoto;
import com.batrakov.service.FileService;
import com.batrakov.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса FileService для работы с документами и фотографиями.
 *
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTool cryptoTool;

    /**
     * Получение документа по хэшу.
     *
     * @param hash Хэш документа
     * @return Документ или null, если документ не найден
     */
    @Override
    public AppDocument getDocument(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocumentRepository.findById(id).orElse(null);
    }

    /**
     * Получение фотографии по хэшу.
     *
     * @param hash Хэш фотографии
     * @return Фотография или null, если фотография не найдена
     */
    @Override
    public AppPhoto getPhoto(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoRepository.findById(id).orElse(null);
    }
}
