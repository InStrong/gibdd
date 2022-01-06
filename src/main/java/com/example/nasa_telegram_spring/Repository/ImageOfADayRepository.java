package com.example.nasa_telegram_spring.Repository;

import com.example.nasa_telegram_spring.Model.Entity.ImageOfADay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageOfADayRepository extends JpaRepository<ImageOfADay, Integer> {
    List<ImageOfADay> findAll();
    ImageOfADay findByDate(String date);
    @Query(value = "select * from image_ofaday order by date desc limit 1", nativeQuery = true)
    ImageOfADay getFirstByDateOrderByDate();
}
