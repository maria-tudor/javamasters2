package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.service.GradeService;
import com.example.javamasters2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping
    public ResponseEntity<?> saveStudent(@Valid @RequestBody Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for student");
        }
        if(student.getStudentId() != null) {
            Optional<Student> postStudent = studentRepository.findById(student.getStudentId());
            if (postStudent.isPresent()) {
                throw new ResourceAlreadyReportedException("student already reported");
            }
        }

        List<Grade> grades;
        if(student.getGradeList() != null && !student.getGradeList().isEmpty()){
            for(Grade grade : student.getGradeList()){
                if(grade.getGradeId() == null){
                    gradeService.saveGrade(grade);
                }
            }
        }

        Student studentFinal = studentService.saveStudent(student);

        return ResponseEntity.ok().body(studentFinal);
    }

    @GetMapping("/studentEnrolledInCourse")
    public ResponseEntity<Subject> isStudentEnrolledInCourse(@RequestParam int studentId,
                                                             @RequestParam int subjectId){
        return ResponseEntity.ok().body(studentService.isStudentEnrolledInCourse(studentId, subjectId));
    }

    @GetMapping("/all/page")
    public ResponseEntity<Page<Student>> retrieveStudents(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("studentSpecialty").ascending());
        Page<Student> studentPage = studentService.retrieveStudents(pageable);
        return ResponseEntity.ok().body(studentPage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> retrieveStudents() {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("studentSpecialty").ascending());
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok().body(students);
    }

    @PutMapping
    public ResponseEntity<?> updateStudent(
            @Valid
            @RequestBody
            Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for student");
        }
        studentService.updateStudent(student);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{studentId}")
    public Student getStudentById(@PathVariable int studentId) {
        Student student = studentService.getStudentById(studentId);
        if(student == null){
            throw new ResourceNotFoundException("student " + studentId + " not found");
        }
        return student;
    }

    @DeleteMapping("{studentId}")
    @ResponseBody
    public void deleteStudentById(@PathVariable int studentId){
        studentService.deleteStudentById(studentId);
    }
}
