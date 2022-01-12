//package com.example.nasa_telegram_spring.Repository;
//
//import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Fine;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//public interface FineRepository extends JpaRepository<Fine, Integer> {
//    @Query(value = "truncate table fine", nativeQuery = true)
//    public void truncateAndResetIdentity();
//
//    Fine findByNumPost(String numPost);
//}
