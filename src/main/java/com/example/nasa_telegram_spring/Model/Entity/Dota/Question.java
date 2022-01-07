package com.example.nasa_telegram_spring.Model.Entity.Dota;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Question {
    String image;
    Map<String, Boolean> answers;
}
