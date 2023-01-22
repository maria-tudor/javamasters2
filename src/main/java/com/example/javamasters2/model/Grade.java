package com.example.javamasters2.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeId;

    private int gradeValue;

    private Date gradeDate;

    @Column(nullable = true)
    private String observations;

    @OneToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Grade(){

    }

    public Grade(int value, Date date, String observations){
        this.gradeValue = value;
        this.gradeDate = date;
        this.observations = observations;
    }

    public Grade(int value, Date date, String observations, Subject subject){
        this(value, date, observations);
        this.subject = subject;
    }

    public Grade(int value, Date date, String observations, Subject subject, Student student){
        this(value, date, observations, subject);
        this.student = student;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(int gradeValue) {
        this.gradeValue = gradeValue;
    }

    public Date getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(Date gradeDate) {
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
