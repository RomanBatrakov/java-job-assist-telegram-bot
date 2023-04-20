package com.batrakov.entity;

import com.batrakov.enums.QuestionState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Представляет сущность вопроса пользователя приложения, которая содержит информацию о вопросе, состоянии вопроса
 * и связи с пользователем.
 * Эта сущность отображается на таблицу "app_user_question" в базе данных.
 *
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user_question")
public class AppUserQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "app_user_telegram_user_id")
    private AppUser appUser;
    private String question;
    @Enumerated(EnumType.STRING)
    private QuestionState questionState;
}
