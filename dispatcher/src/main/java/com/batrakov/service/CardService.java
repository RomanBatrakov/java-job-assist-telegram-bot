package com.batrakov.service;

/**
 * Интерфейс, представляющий сервис для загрузки карт из файла.
 *
 * @version 1.0
 */
public interface CardService {
    /**
     * Метод для загрузки карт из файла.
     *
     * @param fileName Имя файла, из которого производится загрузка карт.
     */
    void loadCards(String fileName);
}
