package com.batrakov.service;

/**
 * Интерфейс для активации пользователей.
 *
 * @version 1.0
 */
public interface UserActivationService {
    /**
     * Активировать пользователя по его идентификатору.
     *
     * @param cryptoUserId Идентификатор пользователя
     * @return true, если активация успешна, иначе false
     */
    boolean activation(String cryptoUserId);
}
