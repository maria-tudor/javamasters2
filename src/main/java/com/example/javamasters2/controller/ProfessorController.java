package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.*;
import com.example.javamasters2.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;


    public ProfessorController(ProfessorService professorService,
                               StudentRepository studentRepository,
                               ProfessorRepository professorRepository,
                               SubjectRepository subjectRepository,
                               DepartmentRepository departmentRepository,
                               CollegeRepository collegeRepository) {
        this.professorService = professorService;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.collegeRepository = collegeRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Professor>> retrieveProfessors() {
        return ResponseEntity.ok().body(professorService.retrieveProfessors());
    }

    @GetMapping("{professorId}")
    public Professor getProfessorById(@PathVariable int professorId) {
        Professor prof = professorService.getProfessorById(professorId);
        if(prof == null){
            throw new ResourceNotFoundException("professor " + professorId + " not found");
        }
        return prof;
    }

    @PostMapping
    public ResponseEntity<?> saveProfessor(@Valid @RequestBody
                                                       Professor professor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for professor");
        }
        if(professor.getProfessorId() != null) {
            Optional<Professor> postProfessor = professorRepository.findById(professor.getProfessorId());
            if (postProfessor.isPresent()) {
                throw new ResourceAlreadyReportedException("professor already reported");
            }
        }

        Integer collegeId = professor.getCollege().getCollegeId();
        Optional<College> postCollege = collegeRepository.findById(collegeId);

        if(postCollege == null || postCollege.isEmpty()){
            throw new ResourceNotFoundException("college " + collegeId + " not found");
        }

        Integer departmentId = professor.getDepartment().getDepartmentId();
        Optional<Department> postDepartment = departmentRepository.findById(departmentId);

        if(postDepartment == null || postDepartment.isEmpty()){
            throw new ResourceNotFoundException("department " + departmentId + " not found");
        }

        Optional<Department> departmentInCollege = departmentRepository.findDepartmentByIdCollege(departmentId, collegeId);

        if(departmentInCollege.isEmpty()){
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }

        List<Student> students = professor.getStudents();
        if(students != null && !students.isEmpty()){
            for(Student student : students){
                if(student.getStudentId() == null){
                    studentRepository.save(student);
                }
            }
        }

        List<Subject> subjects = professor.getSubjects();
        if(subjects != null && !subjects.isEmpty()){
            for(Subject subject : subjects){
                if(subject.getSubjectId() == null){
                    subjectRepository.save(subject);
                }
            }
        }

        Professor profFinal = professorService.saveProfessor(professor);

        return ResponseEntity.ok().body(profFinal);
    }

    @PutMapping
    public ResponseEntity<?> updateProfessor(
            @Valid
            @RequestBody
            Professor professor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for professor");
        }
        professorService.updateProfessor(professor);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{professorId}")
    @ResponseBody
    public void deleteProfessorById(@PathVariable int professorId){
        professorService.deleteProfessorById(professorId);
    }
}
