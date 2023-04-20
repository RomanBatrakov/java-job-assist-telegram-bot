package com.batrakov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий вакансию.
 * Класс содержит поля, геттеры и сеттеры для имени и URL вакансии,
 * а также метод toString() для вывода имени и URL вакансии в виде строки.
 *
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    private String name;
    private String url;

    @Override
    public String toString() {
        return name + "\n" + url;
    }
}
