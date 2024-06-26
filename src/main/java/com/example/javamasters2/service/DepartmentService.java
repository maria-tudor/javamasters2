package com.example.javamasters2.service;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    DepartmentRepository departmentRepository;
    @Autowired
    ProfessorService professorService;

    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    public List<Department> retrieveDepartments(){
        return departmentRepository.findAll();
    }

//    public Department getDepartmentByName(String departmentName){
//        return departmentRepository.findDepartmentByName(departmentName);
//    }

    public Department getDepartmentById(int departmentId){
        return departmentRepository.findDepartmentById(departmentId);
    }

    public Department saveDepartment(Department department){
        return departmentRepository.save(department);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDepartment(Department department) {
        departmentRepository.updateDepartment(department.getDepartmentName(), department.getDepartmentId());
    }

    public void deleteDepartmentById(int departmentId){
        Department department = departmentRepository.findDepartmentById(departmentId);
        if(department != null) {
            deleteDepartmentById(department);
        }
    }

    @Transactional
    public void deleteDepartmentById(Department department){
        professorService.deleteProfessorsFrom(department);
        departmentRepository.deleteById(department.getDepartmentId());
    }
}
