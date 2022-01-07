package com.example.nasa_telegram_spring.Repository;

import com.example.nasa_telegram_spring.Model.Entity.Dota.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DotaAbilityRepository extends JpaRepository<Ability, Integer> {

    @Query(value = "select * from ability order by id desc", nativeQuery = true)
    List<Ability> findAllReverse();

    @Query(value = "select * from ability order by random() limit 1;", nativeQuery = true)
    Ability getRandomAbilty();

    @Query(value = "select dname from ability where dname != :correct order by random() limit 3;", nativeQuery = true)
    List<String> get3RandomAnswers(String correct);
}
