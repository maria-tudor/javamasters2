package com.example.javamasters2;

import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.*;
import com.example.javamasters2.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private GradeService gradeService;
    @Autowired
    private Environment environment;


    public static void main(String[] args) {
        SpringApplication.run(Javamasters2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String activeProfile = this.environment.getActiveProfiles()[0];

        College college1 = new College("Medicina " + activeProfile, "Str. Eroilor 5");
        College college2 = new College("Facultatea de Informatica " + activeProfile, "Str. Bobalna 10");
        College college3 = new College("Facultatea de Litere " + activeProfile, "Str. Independentei 107");

        college1 = collegeRepository.save(college1);
        college2 = collegeRepository.save(college2);
        college3 = collegeRepository.save(college3);

        Department department1College1 = new Department("Interne " + activeProfile);
        Department department2College1 = new Department("Sisteme " + activeProfile);
        Department department1College2 = new Department("Cercetare " + activeProfile);
        Department department2College2 = new Department("AI " + activeProfile);
        Department department3College2 = new Department("Web " + activeProfile);
        Department department1College3 = new Department("Engleza " + activeProfile);

        department1College1.setCollege(college1);
        department2College1.setCollege(college1);
        department1College2.setCollege(college2);
        department2College2.setCollege(college2);
        department3College2.setCollege(college2);
        department1College3.setCollege(college3);

        department1College1 = departmentRepository.save(department1College1);
        department2College1 = departmentRepository.save(department2College1);
        department1College2 = departmentRepository.save(department1College2);
        department2College2 = departmentRepository.save(department2College2);
        department3College2 = departmentRepository.save(department3College2);
        department1College3 = departmentRepository.save(department1College3);

        Professor professor1 = new Professor("Popa Stefan" + " " + activeProfile, "Str Negru Voda 1", "doctor");
        Professor professor2 = new Professor("Ionescu Ion" + " " + activeProfile, "Str Negru Voda 2", "asistent");
        Professor professor3 = new Professor("Tudor Petre " + activeProfile, "Str Negru Voda 3", "doctor");
        Professor professor4 = new Professor("Gheorghe Ana " + activeProfile, "Str Negru Voda 4", "doctorand");
        Professor professor5 = new Professor("Potcovaru Marian" + " " + activeProfile, "Str Negru Voda 5", "masterand");
        Professor professor6 = new Professor("Paul Andreea " + activeProfile, "Str Negru Voda 6", "doctor");
        Professor professor7 = new Professor("Costache Cristina " + activeProfile, "Str Negru Voda 7", "inginer");
        Professor professor8 = new Professor("Ionita Stefania " + activeProfile, "Str Negru Voda 8", "doctor");
        Professor professor9 = new Professor("Marinescu  Ioana " + activeProfile, "Str Negru Voda 9", "asistent");
        Professor professor10 = new Professor("Ion Andrei " + activeProfile, "Str Negru Voda 10", "specialist");
        Professor professor11 = new Professor("Radu Alex " + activeProfile, "Str Negru Voda 11", "doctor");



        professor1.setCollege(college1);
        professor1.setDepartment(department1College1);

        professor2.setCollege(college1);
        professor2.setDepartment(department1College1);

        professor3.setCollege(college1);
        professor3.setDepartment(department2College1);

        professor4.setCollege(college2);
        professor4.setDepartment(department1College2);

        professor5.setCollege(college2);
        professor5.setDepartment(department1College2);

        professor6.setCollege(college2);
        professor6.setDepartment(department2College2);

        professor7.setCollege(college2);
        professor7.setDepartment(department3College2);

        professor8.setCollege(college2);
        professor8.setDepartment(department3College2);

        professor9.setCollege(college2);
        professor9.setDepartment(department3College2);

        professor10.setCollege(college3);
        professor10.setDepartment(department1College3);

        professor11.setCollege(college3);
        professor11.setDepartment(department1College3);



/*        professor1.addStudent(student1);
        professor1.addStudent(student2);

        professor1.addSubject(subject1);
        professor1.addSubject(subject2);*/

        /*professor1.setSubjects(List.of(subject1, subject2));
        professor1.setStudents(List.of(student1, student2));*/


        professor1 = professorRepository.save(professor1);
        professor2 = professorRepository.save(professor2);
        professor3 = professorRepository.save(professor3);
        professor4 = professorRepository.save(professor4);
        professor5 = professorRepository.save(professor5);
        professor6 = professorRepository.save(professor6);
        professor7 = professorRepository.save(professor7);
        professor8 = professorRepository.save(professor8);
        professor9 = professorRepository.save(professor9);
        professor10 = professorRepository.save(professor10);
        professor11 = professorRepository.save(professor11);

        Subject subject1 = new Subject("Farmacologie " + activeProfile, "studiul farmacologiei");
        Subject subject2 = new Subject("Anatomia muschilor " + activeProfile, "studiul anatomiei muschilor");
        Subject subject3 = new Subject("Cercetare in metode de digitalizare " + activeProfile, "studiul digitalizarii");
        Subject subject4 = new Subject("ML " + activeProfile, "studiul anatomiei muschilor");
        Subject subject5 = new Subject("AWBD " + activeProfile, "aplicatii web pentru baze de date");
        Subject subject6 = new Subject("Morfologia limbii " + activeProfile, "studiul morfologiei limbii engleze");

        subject1.setProfessorList(List.of(professor1, professor2));
        subject2.setProfessorList(List.of(professor3));
        subject3.setProfessorList(List.of(professor4, professor5));
        subject4.setProfessorList(List.of(professor6));
        subject5.setProfessorList(List.of(professor7, professor8, professor9));
        subject6.setProfessorList(List.of(professor10, professor11));

        subject1 = subjectRepository.save(subject1);
        subject2 = subjectRepository.save(subject2);
        subject3 = subjectRepository.save(subject3);
        subject4 = subjectRepository.save(subject4);
        subject5 = subjectRepository.save(subject5);
        subject6 = subjectRepository.save(subject6);

        Student student1 = new Student("Tudor Ioana" + " " + activeProfile, "medicina interna", "Str. Luminis 15");
        Student student2 = new Student("Popa Valentin" + " " + activeProfile, "medicina generala", "Str. Tarnavei 1");
        Student student3 = new Student("Ioachim Teodor " + activeProfile, "patologie", "Str. Luminis 16");
        Student student4 = new Student("Scarlat Razvan " + activeProfile, "informatica", "Str. Tarnavei 2");
        Student student5 = new Student("Cristea Claudiu " + activeProfile, "informatica", "Str. Luminis 17");
        Student student6 = new Student("Popa Iustina " + activeProfile, "mate-info", "Str. Tarnavei 3");
        Student student7 = new Student("Petre Laurentiu " + activeProfile, "mate-info", "Str. Luminis 18");
        Student student8 = new Student("Popescu Andrei " + activeProfile, "informatica", "Str. Tarnavei 4");
        Student student9 = new Student("Vlagali Razvan " + activeProfile, "informatica", "Str. Luminis 19");
        Student student10 = new Student("Marin Cornelia " + activeProfile, "informatica", "Str. Tarnavei 5");
        Student student11 = new Student("Ghinea Valentina " + activeProfile, "mate-info", "Str. Luminis 20");
        Student student12 = new Student("Croitoru Catalin " + activeProfile, "romana-engleza", "Str. Tarnavei 6");
        Student student13 = new Student("Stan Adrian " + activeProfile, "romana-engleza", "Str. Luminis 21");
        Student student14 = new Student("Vlasceanu Andreea " + activeProfile, "romana-engleza", "Str. Tarnavei 7");

        student1.setProfessorList(List.of(professor1, professor3));
        student2.setProfessorList(List.of(professor1, professor2));
        student3.setProfessorList(List.of(professor3, professor4));
        student4.setProfessorList(List.of(professor3, professor4, professor5));
        student5.setProfessorList(List.of(professor6, professor7, professor4));
        student6.setProfessorList(List.of(professor6, professor7, professor4));
        student7.setProfessorList(List.of(professor6, professor7, professor4));
        student8.setProfessorList(List.of(professor8, professor9, professor4));
        student9.setProfessorList(List.of(professor8, professor9));
        student10.setProfessorList(List.of(professor5));
        student11.setProfessorList(List.of(professor5, professor9));
        student12.setProfessorList(List.of(professor10, professor11));
        student13.setProfessorList(List.of(professor10));
        student14.setProfessorList(List.of(professor11));

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
        studentRepository.save(student6);
        studentRepository.save(student7);
        studentRepository.save(student8);
        studentRepository.save(student9);
        studentRepository.save(student10);
        studentRepository.save(student11);
        studentRepository.save(student12);
        studentRepository.save(student13);
        studentRepository.save(student14);

        Grade grade1 = new Grade(10, LocalDate.now(), "nimic special " + activeProfile, subject1, student1);
        Grade grade2 = new Grade(7, LocalDate.now(), "", subject2, student1);
        Grade grade3 = new Grade(8, LocalDate.now(), "redare perfecta la 4 " + activeProfile, subject1, student2);
        Grade grade4 = new Grade(5, LocalDate.now(), "nimic special2 " + activeProfile, subject2, student3);
        Grade grade5 = new Grade(2, LocalDate.now(), "nimic special " + activeProfile, subject2, student4);
        Grade grade6 = new Grade(3, LocalDate.now(), "doar grilele " + activeProfile, subject3, student5);
        Grade grade7 = new Grade(10, LocalDate.now(), "rezolvare inovatoare la 5 " + activeProfile, subject3, student6);
        Grade grade8 = new Grade(5, LocalDate.now(), activeProfile, subject4, student6);
        Grade grade9 = new Grade(6, LocalDate.now(), activeProfile, subject5, student5);
        Grade grade10 = new Grade(7, LocalDate.now(), activeProfile, subject5, student6);
        Grade grade11 = new Grade(9, LocalDate.now(), "nimic special " + activeProfile, subject5, student11);
        Grade grade12 = new Grade(1, LocalDate.now(), activeProfile, subject6, student13);
        Grade grade13 = new Grade(10, LocalDate.now(), "10 pagini, explicatii amanuntite " + activeProfile, subject6, student12);
        Grade grade14 = new Grade(7, LocalDate.now(), activeProfile, subject6, student14);

//        grade1.setStudent(student1);
//        grade2.setStudent(student2);
//
//        grade1.setSubject(subject1);
//        grade2.setSubject(subject2);

        gradeService.saveGrade(grade1);
        gradeService.saveGrade(grade2);
        gradeService.saveGrade(grade3);
        gradeService.saveGrade(grade4);
        gradeService.saveGrade(grade5);
        gradeService.saveGrade(grade6);
        gradeService.saveGrade(grade7);
        gradeService.saveGrade(grade8);
        gradeService.saveGrade(grade9);
        gradeService.saveGrade(grade10);
        gradeService.saveGrade(grade11);
        gradeService.saveGrade(grade12);
        gradeService.saveGrade(grade13);
        gradeService.saveGrade(grade14);

    }
}
