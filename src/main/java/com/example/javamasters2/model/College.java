package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int collegeId;

    private String collegeName;

    private String collegeAddress;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<Department> departmentList = new ArrayList<>();

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<Professor> professorList = new ArrayList<>();

    public College(){

    }

    public void addProfessor(Professor professor){
        professorList.add(professor);
        professor.setCollege(this);
    }

    public void addDepartment(Department department){
        departmentList.add(department);
        department.setCollege(this);
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
    }

    public College(String name, String address){
        this.collegeName = name;
        this.collegeAddress = address;
    }


    public College(String name, String address, Optional<List<Department>> departmentList, Optional<List<Professor>> professorList){
        this(name, address);
        if(departmentList.isPresent()){
            this.departmentList = departmentList.get();
        }
        if(professorList.isPresent()){
            this.professorList = professorList.get();
        }
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeAddress() {
        return collegeAddress;
    }

    public void setCollegeAddress(String collegeAddress) {
        this.collegeAddress = collegeAddress;
    }
}
