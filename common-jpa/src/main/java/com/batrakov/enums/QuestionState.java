package com.batrakov.enums;

/**
 * Перечисление, описывающее возможные состояния вопроса в системе.
 * Каждое состояние описывает текущее состояние вопроса и его статус.
 * <p>
 * Возможные значения:
 * - {@link #IN_FAVORITES} - вопрос находится в избранном
 * - {@link #DEFAULT} - вопрос имеет стандартное состояние
 * - {@link #HIDED} - вопрос скрыт
 *
 * @version 1.0
 */
public enum QuestionState {
    IN_FAVORITES,
    DEFAULT,
    HIDED
}
