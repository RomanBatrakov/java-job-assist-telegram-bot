package com.batrakov.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Представляет перечисление категорий карточек, которые используются в приложении.
 * Каждая категория имеет имя и описание.
 *
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum CardCategory {
    ALL_CATEGORY("Все темы", "all"),
    JAVA_CORE("Java core", "core"),
    JAVA_COLLECTION("Java collection", "collection"),
    SPRING("Spring", "spring"),
    SQL("SQL", "sql"),
    HIBERNATE("Hibernate", "hibernate"),
    MULTITHREADING("Multithreading", "multithreading"),
    OTHER("Другие темы", "other");
    private final String name;
    private final String description;

    /**
     * Метод для парсинга строки кнопки и получения соответствующей категории.
     *
     * @param button строка кнопки
     * @return Optional с объектом категории, если удалось найти соответствие, иначе Optional.empty()
     */
    public static Optional<CardCategory> parseCardCategory(String button) {
        if (button.isBlank()) {
            return Optional.empty();
        }
        String formatDescription = button.trim().toLowerCase();
        return Stream.of(values()).filter(c -> c.description.equalsIgnoreCase(formatDescription)).findFirst();
    }
}
