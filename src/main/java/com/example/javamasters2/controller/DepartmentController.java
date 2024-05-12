package com.example.javamasters2.controller;

import com.example.javamasters2.exceptions.BindingResultException;
import com.example.javamasters2.exceptions.ResourceAlreadyReportedException;
import com.example.javamasters2.exceptions.ResourceNotFoundException;
import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.repository.DepartmentRepository;
import com.example.javamasters2.service.DepartmentService;
import com.example.javamasters2.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Department department, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for department");
        }

        if(department.getDepartmentId() != null) {
            Optional<Department> postDepartment = departmentRepository.findById(department.getDepartmentId());

            if (postDepartment.isPresent()) {
                throw new ResourceAlreadyReportedException("department already reported");
            }
        }

        if(department.getProfessorList().size() != 0){
            for(Professor professor : department.getProfessorList()){
                if(professor.getProfessorId() == null){
                    professorService.saveProfessor(professor);
                }
            }
        }

        Integer collegeId = department.getCollege().getCollegeId();
        Optional<College> postCollege = collegeRepository.findById(collegeId);

        if(postCollege.isEmpty() || postCollege == null){
            throw new ResourceNotFoundException("college " + collegeId + " not found");
        }

        if(department.getDepartmentId() != null){
            Optional<Department> dep = departmentRepository.findById(department.getDepartmentId());
            if(dep.isPresent()){
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
            } else{
                Department depFinal = departmentService.saveDepartment(department);
                return ResponseEntity.ok().body(depFinal);
            }
        } else{
            Department depFinal = departmentService.saveDepartment(department);
            return ResponseEntity.ok().body(depFinal);
        }
    }
    
    @GetMapping("all")
    public ResponseEntity<List<Department>> retrieveDepartments(){
        List<Department> departments = departmentService.retrieveDepartments();
        if(departments == null){
            throw new ResourceNotFoundException("departments not found");
        }
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping("{departmentId}")
    public Department getDepartmentById(@PathVariable int departmentId){
        Department department = departmentService.getDepartmentById(departmentId);
        if(department == null){
            throw new ResourceNotFoundException("department " + departmentId + " not found");
        }
        return department;
    }

    @PutMapping
    public ResponseEntity<?> updateDepartment(
            @Valid
            @RequestBody Department department, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BindingResultException("validation errors found for department");
        }
        departmentService.updateDepartment(department);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{departmentId}")
    @ResponseBody
    public void deleteDepartmentById(@PathVariable int departmentId){
        departmentService.deleteDepartmentById(departmentId);
    }
}
