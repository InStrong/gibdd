package com.example.nasa_telegram_spring.Model.Entity.Dota;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CurrentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    String chatId;
    String correctAnswer;

    public CurrentQuestion(String chatId, String correctAnswer) {
        this.chatId = chatId;
        this.correctAnswer = correctAnswer;
    }

    public CurrentQuestion() {
    }
}
