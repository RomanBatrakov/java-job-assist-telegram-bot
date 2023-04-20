package com.batrakov.enums;

/**
 * Перечисление, описывающее возможные состояния пользователя в системе.
 * Каждое состояние описывает текущее состояние пользователя и его действия.
 * <p>
 * Возможные значения:
 * - {@link #BASIC_STATE} - базовое состояние пользователя
 * - {@link #WAIT_FOR_EMAIL_STATE} - ожидание ввода email пользователем
 * - {@link #TRAINING_STATE} - состояние обучения пользователя
 * - {@link #VACANCIES_STATE} - состояние просмотра вакансий пользователем
 * - {@link #EXIT_STATE} - состояние выхода из системы
 *
 * @version 1.0
 */
public enum UserState {
    BASIC_STATE,
    WAIT_FOR_EMAIL_STATE,
    TRAINING_STATE,
    VACANCIES_STATE,
    EXIT_STATE
}
