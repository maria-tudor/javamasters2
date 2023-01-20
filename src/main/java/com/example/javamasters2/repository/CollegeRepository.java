package com.example.javamasters2.repository;

import com.example.javamasters2.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository extends JpaRepository<College, Integer> {

    @Query(value = "select * from college where college_id = :id", nativeQuery = true)
    College findCollegeById(int id);

    @Query(value = "select * from college where college_name = :name", nativeQuery = true)
    College findCollegeByName(String name);

    @Query(value = "select * from college where college_address = :address", nativeQuery = true)
    College findCollegeByAddress(String address);

    @Modifying
    @Query(nativeQuery = true,
            value = "update college coll set coll.college_name = :name where coll.college_id = :id")
    void modifyName(String name, int id);
}
