package com.example.javamasters2.repository;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "select * from subject where subject_id = :id", nativeQuery = true)
    Subject findSubjectById(int id);

    @Query(value = "select * from subject where subject_name = :name", nativeQuery = true)
    Subject findSubjectByName(String name);

    @Modifying
    @Query(nativeQuery = true,
            value = "update subject sub set sub.subject_name = :name, sub.subject_description = :description where sub.subject_id = :id")
    void updateSubject(String name, String description, int id);

    @Query(value = "select * from (subject sub JOIN subject_professor subprof " +
            "ON sub.subject_id = subprof.subject_id)" +
            "where subprof.subject_id = :subject_id and professor_id = :professor_id", nativeQuery = true)
    Optional<List<Subject>> findSubjectProfessorAssociated(Integer subject_id, Integer professor_id);
}
