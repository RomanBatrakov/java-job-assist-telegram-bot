package com.batrakov.utils;

import org.hashids.Hashids;

/**
 * Класс, представляющий инструмент для шифрования и дешифрования значений с использованием библиотеки Hashids.
 *
 * @version 1.0
 */
public class CryptoTool {
    private final Hashids hashids;

    /**
     * Конструктор класса CryptoTool.
     *
     * @param salt Соль для шифрования и дешифрования значений.
     */
    public CryptoTool(String salt) {
        var minHashLength = 10;
        this.hashids = new Hashids(salt, minHashLength);
    }

    /**
     * Получить хэш-значение для заданного числового значения.
     *
     * @param value Числовое значение для шифрования.
     * @return Хэш-значение в виде строки.
     */
    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    /**
     * Получить числовое значение из заданного хэш-значения.
     *
     * @param value Хэш-значение.
     * @return Числовое значение, полученное из хэш-значения.
     */
    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
