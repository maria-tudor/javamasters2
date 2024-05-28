package com.example.javamasters2.controllers;

import com.example.javamasters2.exporter.*;
import com.example.javamasters2.model.*;
import com.example.javamasters2.repository.GradeRepository;
import com.example.javamasters2.service.ProfessorService;
import com.example.javamasters2.util.TestHelper;
import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static com.example.javamasters2.constants.ExceptionConstants.*;
import static com.example.javamasters2.constants.ModelConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private ProfessorService professorService;
    private int gradeIdInserted;
    private String activeProfile;
    private Student student;
    private Subject subject;
    private Grade grade;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;
    private Integer studentIdInserted;
    private Integer subjectIdInserted;
    private Integer gradeValue;
    private LocalDate gradeDate;
    private String observations;

    @BeforeAll
    public void setup() {
        activeProfile = this.environment.getActiveProfiles()[0];
        gradeValue = 5;
        gradeDate = LocalDate.now();
        observations = "observations test" + activeProfile;
        grade = new Grade(gradeValue, gradeDate, observations);

        int collegeId = 0;
        int departmentId = 0;
        int professorId = 0;

        College college = new College("test", "test");
        try {
            Map dataToPostCollege = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college));
            MvcResult collegeResult =
                    mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPostCollege).toString()))
                            .andReturn();

            int responseStatusCollege = collegeResult.getResponse().getStatus();
            if (responseStatusCollege == HttpStatus.OK.value()) {
                JSONObject collegeJSON = new JSONObject(collegeResult.getResponse().getContentAsString());
                collegeId = (int) collegeJSON.get(COLLEGE_ID);
                college.setCollegeId(collegeId);
            }

            Department department = new Department("test");
            department.setCollege(college);

            Map dataToPostDep = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department));
            MvcResult depResult =
                    mockMvc.perform(post("/department/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPostDep).toString()))
                            .andReturn();

            int responseStatusDep = depResult.getResponse().getStatus();
            if (responseStatusDep == HttpStatus.OK.value()) {
                JSONObject depJSON = new JSONObject(depResult.getResponse().getContentAsString());
                departmentId = (int) depJSON.get(DEPARTMENT_ID);
                department.setDepartmentId(departmentId);
            }

            Professor professor = new Professor("test", "test", "test");
            professor.setDepartment(department);
            professor.setCollege(college);

            Map dataToPostProf = TestHelper.convertGsonToFasterXmlMapStyle(ProfessorExporter.exportProfessor(professor));
            MvcResult profResult =
                    mockMvc.perform(post("/professor/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPostProf).toString()))
                            .andReturn();

            int responseStatusProf = profResult.getResponse().getStatus();
            if (responseStatusProf == HttpStatus.OK.value()) {
                JSONObject profJSON = new JSONObject(profResult.getResponse().getContentAsString());
                professorId = (int) profJSON.get(PROFESSOR_ID);
                professor.setProfessorId(professorId);
            }

            student = new Student("student test grade", "specialty test grade", "address test grade");
            subject = new Subject("materie test grade", "descr test grade");
            Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(StudentExporter.exportStudent(student));
            MvcResult studentResult =
                    mockMvc.perform(post("/student/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPost).toString()))
                            .andReturn();

            int responseStatus = studentResult.getResponse().getStatus();
            if (responseStatus == HttpStatus.OK.value()) {
                JSONObject studentJSON = new JSONObject(studentResult.getResponse().getContentAsString());
                studentIdInserted = (int) studentJSON.get(STUDENT_ID);
                student.setStudentId(studentIdInserted);
            }

            grade.setStudent(student);

            Map dataToPostSubject = TestHelper.convertGsonToFasterXmlMapStyle(SubjectExporter.exportSubject(subject));
            MvcResult subjectResult =
                    mockMvc.perform(post("/subject/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPostSubject).toString()))
                            .andReturn();

            int responseStatusSubject = subjectResult.getResponse().getStatus();
            if (responseStatusSubject == HttpStatus.OK.value()) {
                JSONObject subjectJSON = new JSONObject(subjectResult.getResponse().getContentAsString());
                subjectIdInserted = (int) subjectJSON.get(SUBJECT_ID);
                subject.setSubjectId(subjectIdInserted);
            }
            grade.setSubject(subject);
            professorService.addStudentToProfessor(professorId, studentIdInserted);
            professorService.addSubjectToProfessor(professorId, subjectIdInserted);
        } catch (Exception ex) {
            System.out.println("Exception setting up grade controller test");
        }
    }

    @Test
    @Order(1)
    public void addGradeTest() throws Exception{
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String formattedDate = gradeDate.format(formatter);
//        LocalDate formattedLocalDate = LocalDate.parse(formattedDate, formatter);
//        gradeDate = formattedLocalDate;
//        grade.setGradeDate(gradeDate);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade));
        MvcResult gradeResult =
                mockMvc.perform(post("/grade/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = gradeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul addGrade");

        JSONObject gradeJSON = new JSONObject(gradeResult.getResponse().getContentAsString());
        gradeIdInserted = (int) gradeJSON.get(GRADE_ID);
        grade.setGradeId(gradeIdInserted);

        Grade gradeRezultat =  gradeRepository.findGradeById(gradeIdInserted);
        Assert.isTrue(gradeJSON.get(OBSERVATIONS).equals(gradeRezultat.getObservations()),
                "Nu a putut fi obtinut grade in addGradeTest");
    }

    @Test
    @Order(2)
    public void addGradeTestBindingErrorMaxTest() throws Exception{
        grade.setGradeValue(11);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade));
        mockMvc.perform(post("/grade/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));
    }

    @Test
    @Order(3)
    public void addGradeTestBindingErrorMinTest() throws Exception{
        grade.setGradeValue(-1);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade));
        mockMvc.perform(post("/grade/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));
    }

    @Test
    @Order(4)
    public void addGradeTestBindingErrorNullTest() throws Exception{
        grade.setGradeValue(gradeValue);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade));
        dataToPost.remove(GRADE_DATE);
        mockMvc.perform(post("/grade/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));
    }

    @Test
    @Order(5)
    public void addGradeAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade, true));
        mockMvc.perform(post("/grade/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(6)
    public void getGradeByIdTest() throws Exception{
        MvcResult gradeResult =
                mockMvc.perform(get("/grade/" + String.valueOf(gradeIdInserted)))
                        .andReturn();

        int responseStatus = gradeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul getGradeById");

        JSONObject gradeJSON = new JSONObject(gradeResult.getResponse().getContentAsString());
        Assert.isTrue(gradeJSON.get(OBSERVATIONS).equals(observations),
                "Nu a putut fi obtinut grade in getGradeByIdTest");
    }

    @Test
    @Order(7)
    public void getGradeByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/grade/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(8)
    public void updateGradeTest() throws Exception{
        grade.setGradeValue(gradeValue + 1);
        grade.setObservations(observations + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(GradeExporter.exportGrade(grade, true));
        MvcResult gradeResult =
                mockMvc.perform(put("/grade/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = gradeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul updateGrade");

        Grade gradeRezultat =  gradeRepository.findGradeById(gradeIdInserted);
        Assert.isTrue(grade.getObservations().equals(gradeRezultat.getObservations()) &&
                        grade.getGradeValue() == gradeRezultat.getGradeValue(),
                "Nu a putut fi obtinut grade updated in updateGradeTest");
    }

    @Test
    @Order(9)
    public void retrieveGradesTest() throws Exception{
        MvcResult gradeResult =
                mockMvc.perform(get("/grade/all/"))
                        .andReturn();
        int responseStatus = gradeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul retrieveGrades");

        JSONArray gradeJSON = new JSONArray(gradeResult.getResponse().getContentAsString());
        boolean gradeFound = false;
        for(int i = 0; i < gradeJSON.length(); i++){
            JSONObject gradeObject = (JSONObject) gradeJSON.get(i);
            if(((Integer)gradeObject.get(GRADE_ID)) == gradeIdInserted){
                gradeFound = true;
                break;
            }
        }
        Assert.isTrue(gradeFound,
                "Nu a putut fi obtinut grade in retrieveGradesTest");
    }

    @Test
    @Order(10)
    public void deleteGradeByIdTest() throws Exception{
        MvcResult gradeResult =
                mockMvc.perform(delete("/grade/" + String.valueOf(gradeIdInserted)))
                        .andReturn();
        int responseStatus = gradeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul deleteGradeById");

        Grade gradeRezultat =  gradeRepository.findGradeById(gradeIdInserted);
        Assert.isTrue(gradeRezultat == null,
                "A fost gasit grade dupa deleteGradeByIdTest");
    }

}
