package com.example.javamasters2.repository;

import com.example.javamasters2.model.Professor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {

    @Query(value = "select * from professor where professor_id = :id", nativeQuery = true)
    Professor findProfessorById(int id);

    @Query(value = "select * from professor where professor_name = :name", nativeQuery = true)
    List<Professor> findProfessorByName(String name);

    @Modifying
    @Query(nativeQuery = true,
            value = "update professor prof set prof.professor_name = :name where prof.professor_id = :id")
    void modifyName(String name, int id);


}
