package com.example.nasa_telegram_spring.Repository;

import com.example.nasa_telegram_spring.Model.Entity.Dota.CurrentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrentQuestionRepository extends JpaRepository<CurrentQuestion, Integer> {

    @Query(value = "select * from current_question where chat_id = :chatId", nativeQuery = true)
    CurrentQuestion findCorrectByChatId(String chatId);
}
