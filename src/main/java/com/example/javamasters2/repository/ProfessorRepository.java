package com.example.javamasters2.repository;

import com.example.javamasters2.model.Professor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

//    @Transactional
//    @Override
//    public void deleteProfessorById(Integer id) {
//
//        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
//
//        delete(findById(id).orElseThrow(() -> new EmptyResultDataAccessException(
//                String.format("nu a fost gasit profesorul pt delete");
//    }

    @Query(value = "select COUNT(*) from subject_professor where professor_id = :professorId " +
            "AND subject_id = :subjectId", nativeQuery = true)
    int doesProfessorTeachSubject(int professorId, int subjectId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into subject_professor(professor_id, subject_id) " +
                    "VALUES(:professorId, :subjectId)")
    void addSubjectToProfessor(int professorId, int subjectId);

    @Query(value = "select COUNT(*) from student_professor where professor_id = :professorId " +
            "AND student_id = :studentId", nativeQuery = true)
    int doesProfessorTeachStudent(int professorId, int studentId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into student_professor(professor_id, student_id) " +
                    "VALUES(:professorId, :studentId)")
    void addStudentToProfessor(int professorId, int studentId);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM subject_professor WHERE " +
                    "professor_id =:professorId AND subject_id =:subjectId")
    void removeProfessorFromSubject(int professorId, int subjectId);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM subject_professor WHERE " +
                    "professor_id =:professorId")
    void removeProfessorFromAllSubjects(int professorId);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM student_professor WHERE " +
                    "professor_id =:professorId")
    void removeProfessorOfAllStudents(int professorId);


}
