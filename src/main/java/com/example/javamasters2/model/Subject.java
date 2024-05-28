package com.example.javamasters2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subjectId;

    @NotNull
    private String subjectName;

    @NotNull
    private String subjectDescription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subject_professor", joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private List<Professor> professorList = new ArrayList<>();

//    @OneToOne(mappedBy = "subject", cascade = CascadeType.REMOVE)
//    @JsonIgnore
//    private Grade grade;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "subject")
    private List<Grade> gradeList = new ArrayList<>();

    public Subject(){

    }

    public Subject(String name, String description){
        this.subjectName = name;
        this.subjectDescription = description;
    }

    public Subject(String name, String description, List<Professor> professorList){
        this(name, description);
        this.professorList = professorList;
    }

    public void addProfessor(Professor professor){
        professorList.add(professor);
        professor.getSubjects().add(this);
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public List<Professor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Professor> professorList) {
        this.professorList = professorList;
    }

    public List<Grade> getGrades() {
        return gradeList;
    }

    public void setGrades(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}
