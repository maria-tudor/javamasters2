package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @NotNull
    private String studentName;

    @NotNull
    private String studentSpecialty;

    @NotNull
    private String studentAddress;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "student")
    @JsonIgnore
    private List<Grade> gradeList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_professor", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private List<Professor> professorList = new ArrayList<>();

    public Student(){

    }

    public Student(String name, String specialty, String address){
        this.studentName = name;
        this.studentSpecialty = specialty;
        this.studentAddress = address;
    }

    public Student(String name, String specialty, String address, List<Professor> professorList){
        this(name, specialty, address);
        this.professorList = professorList;
    }

    public void addProfessor(Professor professor){
        professorList.add(professor);
        professor.getStudents().add(this);
    }

    public void addGrade(Grade grade){
        gradeList.add(grade);
        grade.setStudent(this);
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSpecialty() {
        return studentSpecialty;
    }

    public void setStudentSpecialty(String studentSpecialty) {
        this.studentSpecialty = studentSpecialty;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
    }
}
