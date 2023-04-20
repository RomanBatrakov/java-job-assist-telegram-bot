package com.batrakov.entity;

import com.batrakov.enums.CardCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Представляет сущность карточки приложения, которая содержит вопрос, ответ и категорию.
 * Эта сущность отображается на таблицу "app_cards" в базе данных.
 *
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_cards",
       uniqueConstraints = {@UniqueConstraint(columnNames = "answer"), @UniqueConstraint(columnNames = "question")})
public class AppCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question", columnDefinition = "TEXT", unique = true)
    private String question;
    @Column(name = "answer", columnDefinition = "TEXT", unique = true)
    private String answer;
    @Enumerated(EnumType.STRING)
    private CardCategory category;
}
