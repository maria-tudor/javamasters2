package com.example.javamasters2.service;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Grade;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeService {

    private GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository){
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> retrieveGrades(){
        return gradeRepository.findAll();
    }

    public Grade getGradeById(int id){
        return gradeRepository.findGradeById(id);
    }

    public Grade saveGrade(Grade grade){
        return gradeRepository.save(grade);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyObservations(Grade grade) {
        gradeRepository.modifyObservations(grade.getObservations(), grade.getGradeId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyValue(Grade grade) {
        gradeRepository.modifyValue(grade.getGradeValue(), grade.getGradeId());
    }

    public void deleteGradeById(int gradeId){
        gradeRepository.deleteById(gradeId);
    }
}
