package com.example.javamasters2.controller;

import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.GradeRepository;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.repository.SubjectRepository;
import com.example.javamasters2.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/grade")
public class GradeController {


    private final GradeService gradeService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;

    public GradeController(GradeService gradeService,
                           StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           GradeRepository gradeRepository){
        this.gradeService = gradeService;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
    }

    @PostMapping("/{gradeId}/{studentId}/{subjectId}")
    public ResponseEntity<Grade> saveGrade(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("gradeId") Integer gradeId,
            @PathVariable("subjectId") Integer subjectId,
            @RequestBody Grade grade) {

        Optional<Grade> postGrade = gradeRepository.findById(gradeId);

        if(postGrade.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        Optional<Student> postStudent = studentRepository.findById(studentId);
        Optional<Subject> postSubject = subjectRepository.findById(subjectId);

        if(postStudent.isEmpty() || postSubject.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student stud = postStudent.get();

        Optional<Grade> grad = gradeRepository.findById(gradeId);
        Grade gradFinal;
        if(grad.isEmpty()){
            gradFinal = gradeService.saveGrade(new Grade(grade.getGradeValue(), grade.getGradeDate(), grade.getObservations(), postSubject.get()));

            stud.addGrade(gradFinal);
            studentRepository.save(stud);

            return ResponseEntity.ok().body(gradFinal);
        } else{
            stud.addGrade(grad.get());

            studentRepository.save(stud);

            return ResponseEntity.ok().body(grad.get());

        }
    }

    @GetMapping
    public ResponseEntity<List<Grade>> retrieveGrades() {
        return ResponseEntity.ok().body(gradeService.retrieveGrades());
    }

    @PutMapping("/observations")
    public void modifyObservations(
            @Valid
            @RequestBody
            Grade grade) {
        gradeService.modifyObservations(grade);
    }

    @PutMapping("/value")
    public void modifyValue(
            @Valid
            @RequestBody
            Grade grade) {
        gradeService.modifyValue(grade);
    }

    @GetMapping("/id")
    public Grade getGradeById(@RequestParam int gradeId) {
        return gradeService.getGradeById(gradeId);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteGradeById(@RequestParam int gradeId){
        gradeService.deleteGradeById(gradeId);
    }
}
