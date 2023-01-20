package com.example.javamasters2.controller;

import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.service.GradeService;
import com.example.javamasters2.service.ProfessorService;
import com.example.javamasters2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/{studentId}")
    public ResponseEntity<Student> saveStudent(@PathVariable Integer studentId,
            @RequestBody Student student) {
        Optional<Student> postStudent = studentRepository.findById(studentId);

        if(postStudent.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        List<Professor> professors;
        if(student.getProfessorList().size() != 0){
            professors = student.getProfessorList().stream().map((professor -> {
                if(professor.getProfessorId() == null){
                    return professorService.saveProfessor(professor);
                }
                return professor;
            })).collect(Collectors.toList());

        }

        List<Grade> grades;
        if(student.getGradeList().size() != 0){
            grades = student.getGradeList().stream().map((grade -> {
                if(grade.getGradeId() == null){
                    return gradeService.saveGrade(grade);
                }
                return grade;
            })).collect(Collectors.toList());

        }

        return ResponseEntity.ok().body(studentService.saveStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<Student>> retrieveStudents() {
        return ResponseEntity.ok().body(studentService.retrieveStudents());
    }

    @PutMapping
    public void modifyName(
            @Valid
            @RequestBody
            Student student) {
        studentService.modifyName(student);
    }

    @GetMapping("/id")
    public Student getCollegeById(@RequestParam int studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping("/name")
    public List<Student> getStudentByName(@RequestParam String studentName) {
        return studentService.getStudentsByName(studentName);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteStudentById(@RequestParam int studentId){
        studentService.deleteStudentById(studentId);
    }
}
