package com.example.javamasters2.repository;

import com.example.javamasters2.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    @Query(value = "select * from grade where grade_id = :id", nativeQuery = true)
    Grade findGradeById(int id);

// FIND GRADES BY DATE

    @Modifying
    @Query(value = "update grade set grade_observations = :observations where grade_id = :id", nativeQuery = true)
    void modifyObservations(String observations, int id);

    @Modifying
    @Query(value = "update grade set grade_value = :value where grade_id = :id", nativeQuery = true)
    void modifyValue(int value, int id);
}
