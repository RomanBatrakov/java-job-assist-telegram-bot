package com.batrakov.dao;

import com.batrakov.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью BinaryContent.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью BinaryContent в базе данных.
 *
 * @version 1.0
 */
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
