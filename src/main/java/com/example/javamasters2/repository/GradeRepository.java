package com.example.javamasters2.repository;

import com.example.javamasters2.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface GradeRepository extends PagingAndSortingRepository<Grade, Integer> {

    @Query(value = "insert into grade(grade_date, grade_value, observations, student_id, subject_id) " +
            "values(TIMESTAMP(:gradeDate), :gradeValue, :observations, :studentId, :subjectId)", nativeQuery = true)
    Grade saveGrade(Long gradeDate, Integer gradeValue, String observations, Integer studentId, Integer subjectId);

    @Query(value = "select * from grade where grade_id = :id", nativeQuery = true)
    Grade findGradeById(int id);

// FIND GRADES BY DATE

    @Modifying
    @Query(value = "update grade set grade_value = :value, observations = :observations  where grade_id = :id", nativeQuery = true)
    void updateGrade(int value, String observations, int id);
}
