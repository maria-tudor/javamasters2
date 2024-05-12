package com.example.javamasters2.service;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Grade;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.repository.GradeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository){
        this.gradeRepository = gradeRepository;
    }

    public Page<Grade> retrieveGrades(Pageable pageable){
        return gradeRepository.findAll(pageable);
    }

    public Grade getGradeById(Integer id){
        return gradeRepository.findGradeById(id);
    }

    public Grade saveGrade(Grade grade){
//        gradeRepository.save()
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
//        grade.setGradeDate(LocalDateTime.from(dateTimeFormatter.parse(LocalDateTime.now().format(dateTimeFormatter))));
        return gradeRepository.save(grade);
//        return gradeRepository.saveGrade(grade.getGradeDate(), grade.getGradeValue(),
//                grade.getObservations(), grade.getStudent().getStudentId(), grade.getSubject().getSubjectId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateGrade(Grade grade) {
        gradeRepository.updateGrade(grade.getGradeValue(), grade.getObservations(), grade.getGradeId());
    }

    public void deleteGradeById(Integer gradeId){
        gradeRepository.deleteById(gradeId);
        if(gradeId != null){
            Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
            if(optionalGrade.isPresent()){
                gradeRepository.delete(optionalGrade.get());
            }
        }
    }
}
