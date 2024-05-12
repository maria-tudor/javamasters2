package com.example.javamasters2.service;

import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.DepartmentRepository;
import com.example.javamasters2.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> retrieveSubjects(){
        return subjectRepository.findAll();
    }

    public Subject getSubjectByName(String subjectName){
        return subjectRepository.findSubjectByName(subjectName);
    }

    public Subject getSubjectById(int subjectId){
        return subjectRepository.findSubjectById(subjectId);
    }

    public Subject saveSubject(Subject subject){
        return subjectRepository.save(subject);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSubject(Subject subject) {
        subjectRepository.updateSubject(subject.getSubjectName(), subject.getSubjectDescription(), subject.getSubjectId());
    }

    public void deleteSubjectById(int subjectId){
        subjectRepository.deleteById(subjectId);
    }
}
