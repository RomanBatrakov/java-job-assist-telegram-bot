package com.batrakov.configuration;

import com.batrakov.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация узла приложения.
 * Класс содержит конфигурацию узла приложения, включая внедрение значения "salt"
 * и создание бина CryptoTool с использованием этого значения.
 *
 * @version 1.0
 */
@Configuration
public class NodeConfiguration {
    @Value("${SALT}")
    private String salt;

    /**
     * Создание бина CryptoTool.
     * Метод создает бин CryptoTool с использованием значения "salt", указанного в конфигурации,
     * и возвращает его.
     *
     * @return Объект CryptoTool.
     */
    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}
