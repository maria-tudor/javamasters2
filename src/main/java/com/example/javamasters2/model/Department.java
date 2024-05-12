package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    @NotNull
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "department")
    @JsonIgnore
    private List<Professor> professorList = new ArrayList<>();

    public Department(){

    }

    public Department(String name){
        this.departmentName = name;
    }

    public void addProfessor(Professor professor){
        professorList.add(professor);
        professor.setDepartment(this);
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
    }
}
