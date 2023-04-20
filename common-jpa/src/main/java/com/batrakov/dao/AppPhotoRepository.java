package com.batrakov.dao;

import com.batrakov.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью AppPhoto.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью AppPhoto в базе данных.
 *
 * @version 1.0
 */
public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}
