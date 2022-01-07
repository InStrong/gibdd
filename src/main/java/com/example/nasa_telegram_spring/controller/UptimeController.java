package com.example.nasa_telegram_spring.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UptimeController {

    @GetMapping("/uptime")
    public ResponseEntity<String> uptimeRobot() {
        return new ResponseEntity<>("Bot is up", HttpStatus.OK);
    }
}
