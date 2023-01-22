package com.example.javamasters2;

import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Javamasters2Application implements CommandLineRunner {
    /**
     * Jpa -> java persistence api
     * <p>
     * JPQL -> java persistence query language
     * <p>
     * Shop - album -> many to many
     * album - album details - one to one
     * artist - album -> one to many
     *
     * @param args
     * @OneToOne, @ManyToMany, @OneToMany, @ManyToOne
     * <p>
     * relatiile se pot defini:
     * - unidirectional
     * - bidirectional
     */

    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private GradeRepository gradeRepository;


    public static void main(String[] args) {
        SpringApplication.run(Javamasters2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        College college1 = new College("Medicina", "Str. Eroilor 5");
        College college2 = new College("Medicina dentara", "Str. Vrabiuta 10");

        collegeRepository.save(college1);
        collegeRepository.save(college2);

        Department department1 = new Department("Ortopedie");
        Department department2 = new Department("Ortodontie");

        department1.setCollege(college1);
        department2.setCollege(college2);

        departmentRepository.save(department1);
        departmentRepository.save(department2);





        Professor professor1 = new Professor("Popescu Stefan", "Str Negru Voda 3", "DOCTOR");

        professor1.setCollege(college1);
        professor1.setDepartment(department1);

/*        professor1.addStudent(student1);
        professor1.addStudent(student2);

        professor1.addSubject(subject1);
        professor1.addSubject(subject2);*/

        /*professor1.setSubjects(List.of(subject1, subject2));
        professor1.setStudents(List.of(student1, student2));*/


        professorRepository.save(professor1);

        Subject subject1 = new Subject("Farmacologie", "studiul farmacologiei");
        Subject subject2 = new Subject("Anatomia muschilor", "studiul anatomiei muschilor");

        subject1.setProfessorList(List.of(professor1));
        subject2.setProfessorList(List.of(professor1));

        subjectRepository.save(subject1);
        subjectRepository.save(subject2);

        Student student1 = new Student("Tudor Ioana", "medicina interna", "Str. Luminis 15");
        Student student2 = new Student("Popa Valentin", "stomatologie", "Str. Tarnavei");

        student1.setProfessorList(List.of(professor1));
        student2.setProfessorList(List.of(professor1));

        studentRepository.save(student1);
        studentRepository.save(student2);

        Grade grade1 = new Grade(10, new Date(), "nimic special", subject1, student1);
        Grade grade2 = new Grade(7, new Date(), "nimic special2");

        grade1.setStudent(student1);
        grade2.setStudent(student2);

        grade1.setSubject(subject1);
        grade2.setSubject(subject2);

        gradeRepository.save(grade1);
        gradeRepository.save(grade2);

    }
}
