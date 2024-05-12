package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer professorId;

    @NotNull
    private String professorName;

    @NotNull
    private String professorRole; //to make enum

    private String professorAddress;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professorList")
    @JsonIgnore
    private List<Subject> subjects = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professorList")
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    public Professor(){

    }

    public Professor(String name, String address, String role){
        this.professorName = name;
        this.professorAddress = address;
        this.professorRole = role;
    }

    public void addStudent(Student student){
        students.add(student);
        student.getProfessorList().add(this);
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
        subject.getProfessorList().add(this);
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getProfessorRole() {
        return professorRole;
    }

    public void setProfessorRole(String professorRole) {
        this.professorRole = professorRole;
    }

    public String getProfessorAddress() {
        return professorAddress;
    }

    public void setProfessorAddress(String professorAddress) {
        this.professorAddress = professorAddress;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
