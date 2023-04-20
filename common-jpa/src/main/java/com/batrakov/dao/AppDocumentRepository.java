package com.batrakov.dao;

import com.batrakov.entity.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью AppDocument.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью AppDocument в базе данных.
 *
 * @version 1.0
 */
public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}
