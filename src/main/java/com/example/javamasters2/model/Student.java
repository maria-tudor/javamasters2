package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    private String studentName;

    private Date studentBirthDate;

    private String studentSpecialty;

    private String studentAddress;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Grade> gradeList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
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

    public Student(String name, String specialty, String address, List<Professor> professorList ){
        this(name, specialty, address);
        this.professorList = professorList;
    }

    public Student(String name, Date birthDate, String specialty, String address){
        this(name, specialty, address);
        this.studentBirthDate = birthDate;
    }

    public void addProfessor(Professor professor){
        professorList.add(professor);
        professor.getStudents().add(this);
    }

    public void addGrade(Grade grade){
        gradeList.add(grade);
        grade.setStudent(this);
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Date getStudentBirthDate() {
        return studentBirthDate;
    }

    public void setStudentBirthDate(Date studentBirthDate) {
        this.studentBirthDate = studentBirthDate;
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
