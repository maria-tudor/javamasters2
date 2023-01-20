package com.example.javamasters2.repository;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query(value = "select * from department where department_id = :id", nativeQuery = true)
    Department findDepartmentById(int id);

    @Query(value = "select * from department where department_name = :name", nativeQuery = true)
    Department findDepartmentByName(String name);

    @Modifying
    @Query(value = "update department set department_name = :name where department_id = :id", nativeQuery = true)
    void modifyName(String name, int id);

    @Query(value = "select * from department where department_id = :id and college_id = :college_id", nativeQuery = true)
    Optional<Department> findDepartmentByIdCollege(Integer id, Integer college_id);
}
