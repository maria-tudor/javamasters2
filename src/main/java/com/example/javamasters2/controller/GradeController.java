package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.GradeRepository;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.repository.SubjectRepository;
import com.example.javamasters2.service.GradeService;
import com.example.javamasters2.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grade")
public class GradeController {

    private final GradeService gradeService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final StudentService studentService;

    public GradeController(GradeService gradeService, StudentService studentService,
                           StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           GradeRepository gradeRepository){
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
    }

    @PostMapping
    public ResponseEntity<?> saveGrade(
            @Valid @RequestBody Grade grade, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for grade");
        }

        if(grade.getGradeId() != null) {
            Optional<Grade> postGrade = gradeRepository.findById(grade.getGradeId());

            if (postGrade.isPresent()) {
                throw new ResourceAlreadyReportedException("grade already reported");
            }
        }

        Optional<Student> postStudent = studentRepository.findById(grade.getStudent().getStudentId());
        Optional<Subject> postSubject = subjectRepository.findById(grade.getSubject().getSubjectId());

        if(postSubject == null || postStudent == null || postStudent.isEmpty() || postSubject.isEmpty()) {
            throw new ResourceNotFoundException("student or subject not found");
        }
        boolean studentAttendsTheCourse = false;
        for(Professor prof : postStudent.get().getProfessorList()){
            if(postSubject.get().getProfessorList().contains(prof)){
                studentAttendsTheCourse = true;
            }
        }

        if(studentService.isStudentEnrolledInCourse(postStudent.get().getStudentId(), postSubject.get().getSubjectId())
                    == null){
            throw new ResourceNotFoundException("Student does not attend the course OR has already been graded");
        }

        Grade gradFinal = gradeService.saveGrade(grade);

        return ResponseEntity.ok().body(gradFinal);
    }

    @GetMapping("all/page")
    public ResponseEntity<Page<Grade>> retrieveGrades(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("gradeValue").ascending());
        Page<Grade> gradesPage = gradeService.retrieveGrades(pageable);
        return ResponseEntity.ok().body(gradesPage);
    }

    @GetMapping("all")
    public ResponseEntity<List<Grade>> retrieveGrades() {
        Iterable<Grade> grades = gradeRepository.findAll();
        return ResponseEntity.ok().body(Streamable.of(grades).toList());
    }

    @PutMapping
    public ResponseEntity<?> updateGrade(
            @Valid
            @RequestBody
            Grade grade, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for grade");
        }
        gradeService.updateGrade(grade);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{gradeId}")
    public Grade getGradeById(@PathVariable int gradeId) {
        Grade grade = gradeService.getGradeById(gradeId);
        if(grade == null){
            throw new ResourceNotFoundException("grade " + gradeId + " not found");
        }
        return grade;
    }

    @DeleteMapping("/{gradeId}")
    public void deleteGradeById(@PathVariable int gradeId){
        gradeService.deleteGradeById(gradeId);
    }
}
