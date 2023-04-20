package com.batrakov.dao;

import com.batrakov.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью AppUser.
 * Интерфейс-наследник от JpaRepository, предоставляющий базовые CRUD-операции
 * для работы с сущностью AppUser в базе данных.
 *
 * @version 1.0
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    /**
     * Удалить пользователя по его Telegram ID.
     *
     * @param telegramUserId Telegram ID пользователя, которого необходимо удалить
     */
    void deleteByTelegramUserId(Long telegramUserId);

    /**
     * Найти пользователя по его Telegram ID.
     *
     * @param id Telegram ID пользователя
     * @return Optional объект с найденным пользователем или пустым значением, если пользователь не найден
     */
    Optional<AppUser> findByTelegramUserId(Long id);

    /**
     * Найти пользователя по его адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя
     * @return Optional объект с найденным пользователем или пустым значением, если пользователь не найден
     */
    Optional<AppUser> findByEmail(String email);
}
