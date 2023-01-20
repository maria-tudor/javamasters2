package com.example.javamasters2.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
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

    @PostMapping("/{departmentId}/{collegeId}")
    public ResponseEntity<Department> save( @PathVariable("collegeId") Integer collegeId,
                                            @PathVariable("departmentId") Integer departmentId,
            @RequestBody Department department){

        Optional<Department> postDepartment = departmentRepository.findById(departmentId);

        if(postDepartment.isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        List<Professor> professors;
        if(department.getProfessorList().size() != 0){
            professors = department.getProfessorList().stream().map((professor -> {
                if(professor.getProfessorId() == null){
                    return professorService.saveProfessor(professor);
                }
                return professor;
            })).collect(Collectors.toList());

        }

        Optional<College> postCollege = collegeRepository.findById(collegeId);

        if(postCollege.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        College coll = postCollege.get();

        Optional<Department> dep = departmentRepository.findById(departmentId);
        Department depFinal;
        if(dep.isEmpty()){
            depFinal = departmentService.saveDepartment(new Department(department.getDepartmentName()));

            coll.addDepartment(depFinal);
            collegeRepository.save(coll);

            return ResponseEntity.ok().body(depFinal);
        } else{
            coll.addDepartment(dep.get());
            collegeRepository.save(coll);

            return ResponseEntity.ok().body(dep.get());

        }
    }
    
    @GetMapping
    public ResponseEntity<List<Department>> retrieveDepartments(){
        return ResponseEntity.ok().body(departmentService.retrieveDepartments());
    }

    @GetMapping("/id")
    public Department getDepartmentById(@RequestParam int departmentId){
        return departmentService.getDepartmentById(departmentId);
    }

    @GetMapping("/name")
    public Department getDepartmentByName(@RequestParam String departmentName){
        return departmentService.getDepartmentByName(departmentName);
    }

    @PutMapping
    public void modifyName(
            @Valid
            @RequestBody Department department){
        departmentService.modifyName(department);
    }

    @DeleteMapping("/id")
    @ResponseBody
    public void deleteDepartmentById(@RequestParam int departmentId){
        departmentService.deleteDepartmentById(departmentId);
    }
}
