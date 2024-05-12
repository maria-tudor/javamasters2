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
            value = "update professor prof set prof.professor_name = :name, " +
                    "prof.professor_address = :address, " +
                    "prof.professor_role = :role " +
                    "where prof.professor_id = :id")
    void updateProfessor(String name, String address, String role, int id);


}
