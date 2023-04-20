package com.batrakov.configuration;

import com.batrakov.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация REST-сервиса.
 * Определяет бин CryptoTool с использованием настройки salt из application.properties.
 *
 * @version 1.0
 */
@Configuration
public class RestServiceConfiguration {
    @Value("${SALT}")
    private String salt;

    /**
     * Создает и возвращает бин CryptoTool с использованием настройки salt.
     *
     * @return Бин CryptoTool.
     */
    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}
