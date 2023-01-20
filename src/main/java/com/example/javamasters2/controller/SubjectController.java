package com.example.javamasters2.controller;

import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.SubjectRepository;
import com.example.javamasters2.service.ProfessorService;
import com.example.javamasters2.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping("/{subjectId}")
    public ResponseEntity<Subject> saveSubject(@PathVariable Integer subjectId,
            @RequestBody Subject subject) {

        Optional<Subject> postSubject = subjectRepository.findById(subjectId);

        if(postSubject.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        List<Professor> professors;
        if(subject.getProfessorList().size() != 0){
            professors = subject.getProfessorList().stream().map((professor -> {
                if(professor.getProfessorId() == null){
                    return professorService.saveProfessor(professor);
                }
                return professor;
            })).collect(Collectors.toList());

        }
        return ResponseEntity.ok().body(subjectService.saveSubject(subject));
    }

    @GetMapping
    public ResponseEntity<List<Subject>> retrieveSubjects() {
        return ResponseEntity.ok().body(subjectService.retrieveSubjects());
    }

    @GetMapping("/id")
    public Subject getSubjectById(@RequestParam int subjectId) {
        return subjectService.getSubjectById(subjectId);
    }

    @GetMapping("/name")
    public Subject getSubjectByName(@RequestParam String subjectName) {
        return subjectService.getSubjectByName(subjectName);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteSubjectById(@RequestParam int subjectId){
        subjectService.deleteSubjectById(subjectId);
    }

    @PutMapping
    public void modifyName(
            @Valid
            @RequestBody
            Subject subject) {
        subjectService.modifyName(subject);
    }
}
