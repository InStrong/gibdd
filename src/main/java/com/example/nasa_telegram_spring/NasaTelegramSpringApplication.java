package com.example.nasa_telegram_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NasaTelegramSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(NasaTelegramSpringApplication.class, args);
    }

}
