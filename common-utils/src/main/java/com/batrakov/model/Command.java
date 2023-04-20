package com.batrakov.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Перечисление, представляющее команды бота.
 * Каждая команда имеет название и описание.
 *
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum Command {
    START("/start", "Привет, Джаб!"),
    REGISTRATION("/registration", "Регистрация"),
    VACANCIES("/vacancies", "Вакансии"),
    FAVOURITES("/favourites", "Избранные вакансии"),
    TRAINING("/training", "Прокачаться"),
    FAQ("/faq", "Чё тут происходит?"),
    CANCEL("/cancel", "Отменить операцию"),
    EXIT("/exit", "Хочу уйти, удали мои данные");

    private final String name;
    private final String description;

    /**
     * Метод для парсинга строки с командой и получения соответствующего значения перечисления Command.
     *
     * @param command Строковое представление команды.
     * @return Значение перечисления Command, соответствующее переданной строке, если найдено.
     */
    public static Optional<Command> parseCommand(String command) {
        if (command.isBlank()) {
            return Optional.empty();
        }
        String formatName = command.trim().toLowerCase();
        return Stream.of(values())
                     .filter(c -> c.name.equalsIgnoreCase(formatName) ||
                             c.description.toLowerCase().contains(formatName))
                     .findFirst();
    }
}
