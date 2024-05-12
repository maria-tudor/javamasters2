package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.ProfessorRepository;
import com.example.javamasters2.repository.SubjectRepository;
import com.example.javamasters2.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping
    public ResponseEntity<?> saveSubject(@Valid @RequestBody Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for subject");
        }
        if(subject.getSubjectId() != null) {
            Optional<Subject> postSubject = subjectRepository.findById(subject.getSubjectId());

            if (postSubject.isPresent()) {
                throw new ResourceAlreadyReportedException("subject already reported");
            }
        }

        if(subject.getProfessorList() != null && subject.getProfessorList().size() != 0){
            for(Professor professor : subject.getProfessorList()){
                if(professor.getProfessorId() == null){
                    professorRepository.save(professor);
                }
            }
        }

        Subject subjectFinal = subjectService.saveSubject(subject);
        return ResponseEntity.ok().body(subjectFinal);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Subject>> retrieveSubjects() {
        List<Subject> subjects = subjectService.retrieveSubjects();
        return ResponseEntity.ok().body(subjects);
    }

    @GetMapping("{subjectId}")
    public Subject getSubjectById(@PathVariable int subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        if(subject == null){
            throw new ResourceNotFoundException("subject " + subjectId + " not found");
        }
        return subject;
    }

    @DeleteMapping("{subjectId}")
    @ResponseBody
    public void deleteSubjectById(@PathVariable int subjectId){
        subjectService.deleteSubjectById(subjectId);
    }

    @PutMapping
    public ResponseEntity<?> updateSubject(
            @Valid
            @RequestBody
            Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for subject");
        }
        subjectService.updateSubject(subject);

        return ResponseEntity.ok().build();
    }
}
