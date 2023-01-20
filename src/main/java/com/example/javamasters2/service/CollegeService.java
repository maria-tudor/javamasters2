package com.example.javamasters2.service;

import com.example.javamasters2.model.College;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollegeService {
    private CollegeRepository collegeRepository;
    private final StudentRepository studentRepository;

    public CollegeService(CollegeRepository collegeRepository,
                          StudentRepository studentRepository){
        this.collegeRepository = collegeRepository;
        this.studentRepository = studentRepository;
    }

    public List<College> retrieveColleges(){
        return collegeRepository.findAll();
    }

    public College getCollegeById(int id){
        return collegeRepository.findCollegeById(id);
    }

    public College getCollegeByAddress(String collegeAddress){
        return collegeRepository.findCollegeByAddress(collegeAddress);
    }

    public College getCollegeByName(String collegeName){
        return collegeRepository.findCollegeByName(collegeName);
    }

    public College saveCollege(College college){
        return collegeRepository.save(college);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyName(College college) {
        collegeRepository.modifyName(college.getCollegeName(), college.getCollegeId());
    }

    public void deleteCollegeById(int collegeId){
        collegeRepository.deleteById(collegeId);
    }

    public void addStudentToCourse(int studentId, int subjectId){

    }
}
