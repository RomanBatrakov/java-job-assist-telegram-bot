package com.batrakov.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Перечисление, представляющее кнопки, используемые в приложении.
 * Каждая кнопка имеет имя и описание.
 *
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum Button {
    ANSWER_BUTTON("Ответ", "Ответ"),
    QUESTION_BUTTON("Вопрос", "Вопрос"),
    PREVIOUS_BUTTON("⬅️", "Previous"),
    NEXT_BUTTON("➡️", "Next"),
    FAVOURITE_BUTTON("❤️", "Favourite"),
    HIDE_BUTTON("\uD83D\uDC94", "Hide"),
    HIDE_QUESTION_BUTTON("\uD83D\uDC4E", "Скрыть"),
    YES_EXIT_BUTTON("Да, уйти", "Удаление данных"),
    NO_EXIT_BUTTON("Нет, остаться", "Продолжить работу");

    private final String name;
    private final String description;

    /**
     * Метод для парсинга строки с описанием кнопки и получения соответствующего значения перечисления Button.
     *
     * @param button Строковое представление кнопки.
     * @return Значение перечисления Button, соответствующее переданной строке, если найдено.
     */
    public static Optional<Button> parseButton(String button) {
        if (button.isBlank()) {
            return Optional.empty();
        }
        String formatDescription = button.trim().toLowerCase();
        return Stream.of(values()).filter(c -> c.description.equalsIgnoreCase(formatDescription)).findFirst();
    }
}
