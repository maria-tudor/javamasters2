package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.repository.DepartmentRepository;
import com.example.javamasters2.service.CollegeService;
import com.example.javamasters2.service.DepartmentService;
import com.example.javamasters2.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/college")
public class CollegeController {
    @Autowired
    private final CollegeService collegeService;
    @Autowired
    private final DepartmentService departmentService;
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final CollegeRepository collegeRepository;

    public CollegeController(CollegeService collegeService, DepartmentService departmentService, ProfessorService professorService,
                             CollegeRepository collegeRepository) {
        this.collegeService = collegeService;
        this.departmentService = departmentService;
        this.professorService = professorService;
        this.collegeRepository = collegeRepository;
    }


    @PostMapping
    public ResponseEntity<?> saveCollege(@Valid @RequestBody College college, BindingResult bindingResult) { /// TODO change request for addCollege
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for college");
        }

        if(college.getCollegeId() != null) {
            Optional<College> postCollege = collegeRepository.findById(college.getCollegeId());

            if (postCollege.isPresent()) {
                throw new ResourceAlreadyReportedException("college already reported");
            }
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

    @GetMapping("/all")
    public ResponseEntity<List<College>> retrieveColleges() {
        List<College> colleges = collegeService.retrieveColleges();
        if(colleges == null){
            throw new ResourceNotFoundException("colleges not found");
        }
        return ResponseEntity.ok().body(colleges);
    }

    @PutMapping
    public ResponseEntity<?> updateCollege(
            @Valid
            @RequestBody
            College college, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for college");
        }
        collegeService.updateCollege(college);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{collegeId}")
    public College getCollegeById(@PathVariable int collegeId) {
        College college = collegeService.getCollegeById(collegeId);
        if(college == null){
            throw new ResourceNotFoundException("college " + collegeId + " not found");
        }
        return college;
    }

    @DeleteMapping("{collegeId}")
    @ResponseBody
    public void deleteCollegeById(@PathVariable String collegeId){
        Integer collegeIdInt = Integer.parseInt(collegeId);
        College college = collegeService.getCollegeById(collegeIdInt);
        if(college == null){
            throw new ResourceNotFoundException("college " + collegeId + " not found");
        }

        int numberOfDept = college.getDepartmentList().size();
        for(int i = 0; i < numberOfDept; i++){
            Department departmentCurent = college.getDepartmentList().get(i);
            professorService.deleteProfessorsFrom(departmentCurent);
        }
        collegeService.deleteCollegeById(collegeIdInt);
    }

    @PutMapping("/studentCourse")
    public ResponseEntity<?> addStudentToCourse(
            @Valid
            @RequestBody
            int studentId, int subjectId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for college");
        }
        collegeService.addStudentToCourse(studentId, subjectId);

        return ResponseEntity.ok().build();
    }

}
