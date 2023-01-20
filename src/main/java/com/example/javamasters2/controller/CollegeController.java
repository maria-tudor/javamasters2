package com.example.javamasters2.controller;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.service.CollegeService;
import com.example.javamasters2.service.DepartmentService;
import com.example.javamasters2.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/college")
public class CollegeController {
    @Autowired
    private final CollegeService collegeService;
    @Autowired
    private final DepartmentService departmentService;

    @Autowired
    private final ProfessorService professorService;
    private final CollegeRepository collegeRepository;

    public CollegeController(CollegeService collegeService, DepartmentService departmentService, ProfessorService professorService,
                             CollegeRepository collegeRepository) {
        this.collegeService = collegeService;
        this.departmentService = departmentService;
        this.professorService = professorService;
        this.collegeRepository = collegeRepository;
    }


    @PostMapping("/{collegeId}")
    public ResponseEntity<College> saveCollege(@PathVariable Integer collegeId,
            @RequestBody College college) {

        Optional<College> postCollege = collegeRepository.findById(collegeId);

        if(postCollege.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        List<Department> departments;
        if(college.getDepartmentList().size() != 0){
            departments = college.getDepartmentList().stream().map((department -> {
                if(department.getDepartmentId() == null){
                    return departmentService.saveDepartment(department);
                }
                return department;
            })).collect(Collectors.toList());

        }

        List<Professor> professors;
        if(college.getProfessorList().size() != 0){
            professors = college.getProfessorList().stream().map((professor -> {
                if(professor.getProfessorId() == null){
                    return professorService.saveProfessor(professor);
                }
                return professor;
            })).collect(Collectors.toList());

        }

        return ResponseEntity.ok().body(collegeService.saveCollege(college));
    }

    @GetMapping
    public ResponseEntity<List<College>> retrieveColleges() {
        return ResponseEntity.ok().body(collegeService.retrieveColleges());
    }

    @PutMapping
    public void modifyName(
            @Valid
            @RequestBody
            College college) {
        collegeService.modifyName(college);
    }

    @GetMapping("/id")
    public College getCollegeById(@RequestParam int collegeId) {
        return collegeService.getCollegeById(collegeId);
    }

    @GetMapping("/address")
    public College getCollegeByAddress(@RequestParam String collegeAddress) {
        return collegeService.getCollegeByAddress(collegeAddress);
    }

    @GetMapping("/name")
    public College getCollegeByName(@RequestParam String collegeName) {
        return collegeService.getCollegeByName(collegeName);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteCollegeById(@RequestParam int collegeId){
        collegeService.deleteCollegeById(collegeId);
    }

    @PutMapping("/studentCourse")
    public void addStudentToCourse(
            @Valid
            @RequestBody
            int studentId, int subjectId) {
        collegeService.addStudentToCourse(studentId, subjectId);
    }

}
