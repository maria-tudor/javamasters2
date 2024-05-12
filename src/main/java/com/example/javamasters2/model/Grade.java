package com.example.javamasters2.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeId;

    @Min(value=1, message ="min grade 1")
    @Max(value=10, message ="max grade 10")
    @NotNull
    private Integer gradeValue;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @NotNull
    private LocalDate gradeDate;

    @Column(nullable = true)
    private String observations;

    @OneToOne()
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Grade(){

    }

    public Grade(int value, LocalDate date, String observations){
        this.gradeValue = value;
        this.gradeDate = date;
        this.observations = observations;
    }

    public Grade(int value, LocalDate date, String observations, Subject subject){
        this(value, date, observations);
        this.subject = subject;
    }

    public Grade(int value, LocalDate date, String observations, Subject subject, Student student){
        this(value, date, observations, subject);
        this.student = student;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Integer gradeValue) {
        this.gradeValue = gradeValue;
    }

    public LocalDate getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(LocalDate gradeDate) {
        this.gradeDate = gradeDate;
    }

    public String /*sau Optional*/ getObservations() {
        return observations;
        // return Optional.ofNullable(publishingDate);
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
