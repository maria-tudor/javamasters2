package com.example.javamasters2.repository;

import com.example.javamasters2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(value = "select * from student where student_id = :id", nativeQuery = true)
    Student findStudentById(int id);

    @Query(value = "select * from student where student_name = :name", nativeQuery = true)
    List<Student> findStudentsByName(String name);

    @Modifying
    @Query(nativeQuery = true,
            value = "update student stu set stu.student_name = :name where stu.student_id = :id")
    void modifyName(String name, int id);

    @Query(value = "select * from (student stud JOIN student_professor studprof " +
            "ON stud.student_id = studprof.student_id)" +
            "where studprof.student_id = :student_id and professor_id = :professor_id", nativeQuery = true)
    Optional<List<Student>> findStudentProfessorAssociated(Integer student_id, Integer professor_id);

}
