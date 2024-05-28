package com.example.javamasters2.service;

import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessorService {

    private ProfessorRepository professorRepository;
    private DepartmentRepository departmentRepository;
    private SubjectRepository subjectRepository;
    private StudentRepository studentRepository;
    private final CollegeRepository collegeRepository;

    public ProfessorService(ProfessorRepository professorRepository, CollegeRepository collegeRepository, DepartmentRepository departmentRepository, SubjectRepository subjectRepository, StudentRepository studentRepository) {
        this.professorRepository = professorRepository;
        this.collegeRepository = collegeRepository;
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
    }

    public List<Professor> retrieveProfessors() {
        return professorRepository.findAll();
    }

    public Professor saveProfessor(Professor professor){//}, Integer collegeId, Integer departmentId, List<Integer> subjectIds, List<Integer> studentIds) {
/*        College college = collegeRepository.findCollegeById(collegeId);
        professor.setCollege(college);

        Department department = departmentRepository.findDepartmentById(departmentId);
        professor.setDepartment(department);

        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        professor.setSubjects(subjects);

        List<Student> students = studentRepository.findAllById(studentIds);
        professor.setStudents(students);*/
        return professorRepository.save(professor);
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProfessor(Professor professor) {
        professorRepository.updateProfessor(professor.getProfessorName(), professor.getProfessorAddress(),
                professor.getProfessorRole(), professor.getProfessorId());
    }

    public void deleteProfessorById(int professorId){
        professorRepository.deleteById(professorId);
    }

    public List<Professor> getProfessorsByName(String professorName){
        return professorRepository.findProfessorByName(professorName);
    }

    public Professor getProfessorById(int professorId){
        return professorRepository.findProfessorById(professorId);
    }

    public boolean doesProfessorTeachSubject(int professorId, int subjectId){
        return professorRepository.doesProfessorTeachSubject(professorId, subjectId) == 1;
    }

    @Transactional
    public void addSubjectToProfessor(int professorId, int subjectId){
        professorRepository.addSubjectToProfessor(professorId, subjectId);
    }

    public boolean doesProfessorTeachStudent(int professorId, int studentId){
        return professorRepository.doesProfessorTeachStudent(professorId, studentId) == 1;
    }

    @Transactional
    public void addStudentToProfessor(int professorId, int studentId){
        professorRepository.addStudentToProfessor(professorId, studentId);
    }

    @Transactional
    public void removeProfessorFromSubject(int professorId, int subjectId){
        professorRepository.removeProfessorFromSubject(professorId, subjectId);
    }

    @Transactional
    public void removeProfessorFromAllSubjects(int professorId){
        professorRepository.removeProfessorFromAllSubjects(professorId);
    }

    @Transactional
    public void removeProfessorOfAllStudents(int professorId){
        professorRepository.removeProfessorOfAllStudents(professorId);
    }

    @Transactional
    public void deleteProfessorsFrom(Department department){
        int numberOfProf = department.getProfessorList().size();
        for(int i = 0; i < numberOfProf; i++){
            int professorIdCurent = department.getProfessorList().get(i).getProfessorId();
            removeProfessorFromAllSubjects(professorIdCurent);
            removeProfessorOfAllStudents(professorIdCurent);
        }
    }
}
