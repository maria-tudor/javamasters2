package com.example.javamasters2.controller;

import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.*;
import com.example.javamasters2.service.ProfessorService;
import javax.validation.Valid;

import com.example.javamasters2.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;


    public ProfessorController(ProfessorService professorService,
                               StudentService studentService,
                               StudentRepository studentRepository,
                               ProfessorRepository professorRepository,
                               SubjectRepository subjectRepository,
                               DepartmentRepository departmentRepository,
                               CollegeRepository collegeRepository) {
        this.professorService = professorService;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.collegeRepository = collegeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> retrieveProfessors() {
        return ResponseEntity.ok().body(professorService.retrieveProfessors());
    }

    @GetMapping("/id")
    public Professor getProfessorById(@RequestParam int professorId) {
        return professorService.getProfessorById(professorId);
    }

    @GetMapping("/name")
    public List<Professor> getProfessorsByName(@RequestParam String professorName) {
        return professorService.getProfessorsByName(professorName);
    }

    @PostMapping("/{professorId}/{collegeId}/{departmentId}/{studentId}/{subjectId}")
    public ResponseEntity<Professor> saveProfessor(@PathVariable("studentId") Integer studentId,
                                        @PathVariable("professorId") Integer professorId,
                                        @PathVariable("subjectId") Integer subjectId,
                                        @PathVariable("departmentId") Integer departmentId,
                                        @PathVariable("collegeId") Integer collegeId,
                                        @RequestBody Professor professor/*,
                                         @RequestParam Integer collegeId,
                                         @RequestParam Integer departmentId,
                                         @RequestParam List<Integer> subjectIds,
                                         @RequestParam List<Integer> studentIds*/
    ) {
        Optional<Professor> postProfessor = professorRepository.findById(professorId);
        if(postProfessor.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        Optional<Student> postStudent = studentRepository.findById(studentId);
        Optional<Subject> postSubject = subjectRepository.findById(subjectId);
        Optional<Department> postDepartment = departmentRepository.findById(departmentId);
        Optional<College> postCollege = collegeRepository.findById(collegeId);

        if(postStudent.isEmpty() || postSubject.isEmpty() || postDepartment.isEmpty() || postCollege.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student stud = postStudent.get();
        Subject sub = postSubject.get();
        Department dep = postDepartment.get();
        College coll = postCollege.get();

        Optional<Department> departmentInCollege = departmentRepository.findDepartmentByIdCollege(departmentId, collegeId);

        if(departmentInCollege.isEmpty()){
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }

        Optional<List<Student>> studentAssocProf = studentRepository.findStudentProfessorAssociated(studentId, professorId);
        Optional<List<Subject>> subjectAssocProf = subjectRepository.findSubjectProfessorAssociated(subjectId, professorId);

        // pentru a nu se scrie asocierea de mai multe ori in
        if(studentAssocProf.isPresent() && studentAssocProf.get().size() >= 1 ||
                (subjectAssocProf.isPresent() && subjectAssocProf.get().size() >= 1)){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        Optional<Professor> prof = professorRepository.findById(professorId);
        Professor profFinal;
        if(prof.isEmpty()){
            profFinal = professorService.saveProfessor(new Professor(professor.getProfessorName(), professor.getProfessorAddress(), professor.getProfessorRole()));

            stud.addProfessor(profFinal);
            sub.addProfessor(profFinal);
            dep.addProfessor(profFinal);
            coll.addProfessor(profFinal);

            studentRepository.save(stud);
            subjectRepository.save(sub);
            departmentRepository.save(dep);
            collegeRepository.save(coll);

            return ResponseEntity.ok().body(profFinal);
        } else{
            stud.addProfessor(prof.get());
            sub.addProfessor(prof.get());
            dep.addProfessor(prof.get());
            coll.addProfessor(prof.get());

            studentRepository.save(stud);
            subjectRepository.save(sub);
            departmentRepository.save(dep);
            collegeRepository.save(coll);

            return ResponseEntity.ok().body(prof.get());

        }





        ///return ResponseEntity.ok().body(professorService.saveProfessor(professor));//, collegeId, departmentId, subjectIds, studentIds));
    }

    public void checkForSubject(Subject subject){

    }

    @PutMapping
    public void modifyName(
            @Valid
            @RequestBody
            Professor professor) {
        professorService.modifyName(professor);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteProfessorById(@RequestParam int professorId){
        professorService.deleteProfessorById(professorId);
    }
}
