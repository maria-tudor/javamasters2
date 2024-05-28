package com.example.javamasters2.service;

import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public StudentService(StudentRepository studentRepository, SubjectRepository subjectRepository){
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Student> retrieveStudents(){
        return studentRepository.findAll();
    }

    public Page<Student> retrieveStudents(Pageable pageable){
        return studentRepository.findAll(pageable);
    }

    public List<Student> getStudentsByName(String studentName){
        return studentRepository.findStudentsByName(studentName);
    }

    public Student getStudentById(int studentId){
        return studentRepository.findStudentById(studentId);
    }

    public Student saveStudent(Student student){
        return studentRepository.save(student);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStudent(Student student) {
        studentRepository.updateStudent(student.getStudentAddress(),
                student.getStudentName(), student.getStudentSpecialty(), student.getStudentId());
    }

    public void deleteStudentById(int studentId){
        studentRepository.deleteById(studentId);
    }

    public Subject isStudentEnrolledInCourse(int studentId, int subjectId){
        boolean isStudentEnrolled =  studentRepository.isStudentEnrolledInCourse(studentId, subjectId) == 1;
        boolean hasStudentReceivedGrade = studentRepository.hasStudentReceivedGrade(studentId, subjectId) == 1;
        if(isStudentEnrolled && !hasStudentReceivedGrade){
            return subjectRepository.findSubjectById(subjectId);
        }
        return null;
    }
}
