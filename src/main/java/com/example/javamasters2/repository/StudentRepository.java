package com.example.javamasters2.repository;

import com.example.javamasters2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
            value = "update student stu set stu.student_name = :name, " +
                    "stu.student_address = :address, " +
                    "stu.student_specialty = :specialty " +
                    " where stu.student_id = :id")
    void updateStudent(String address, String name, String specialty, int id);

    @Query(value = "select * from (student stud JOIN student_professor studprof " +
            "ON stud.student_id = studprof.student_id)" +
            "where studprof.student_id = :student_id and professor_id = :professor_id", nativeQuery = true)
    Optional<List<Student>> findStudentProfessorAssociated(Integer student_id, Integer professor_id);

    @Query(value = "SELECT COUNT(*) FROM student s " +
            "JOIN student_professor sp " +
            "ON sp.student_id = s.student_id " +
            "JOIN professor p " +
            "ON p.professor_id = sp.professor_id " +
            "JOIN subject_professor subp " +
            "ON subp.professor_id = p.professor_id " +
            "JOIN subject sub " +
            "ON sub.subject_id = subp.subject_id " +
            "WHERE sub.subject_id = :subjectId " +
            "AND s.student_id = :studentId", nativeQuery = true)
    int isStudentEnrolledInCourse(int studentId, int subjectId);

    @Query(value = "SELECT COUNT(*) FROM grade " +
            "WHERE subject_id = :subjectId " +
            "AND student_id = :studentId", nativeQuery = true)
    int hasStudentReceivedGrade(int studentId, int subjectId);

}
