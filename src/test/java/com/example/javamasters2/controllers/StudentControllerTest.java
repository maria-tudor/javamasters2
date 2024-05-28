package com.example.javamasters2.controllers;

import com.example.javamasters2.exporter.StudentExporter;
import com.example.javamasters2.model.Student;
import com.example.javamasters2.repository.StudentRepository;
import com.example.javamasters2.util.TestHelper;

import com.example.javamasters2.exporter.CollegeExporter;
import com.example.javamasters2.model.College;
import com.example.javamasters2.repository.CollegeRepository;
import com.example.javamasters2.util.TestHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

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
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private StudentRepository studentRepository;
    private int studentIdInserted;
    private String activeProfile;
    private Student student;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;
    private String studentName;
    private String studentAddress;
    private String studentSpecialty;

    @BeforeAll
    public void setup(){
        activeProfile = this.environment.getActiveProfiles()[0];
    }

    @Test
    @Order(1)
    public void addStudentTest() throws Exception{
        studentName = "Student  " + activeProfile;
        studentAddress = "adresa student test";
        studentSpecialty = "specialitate test";
        student = new Student(studentName, studentSpecialty, studentAddress);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(StudentExporter.exportStudent(student));
        MvcResult studentResult =
                mockMvc.perform(post("/student/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = studentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul addStudent");

        JSONObject studentJSON = new JSONObject(studentResult.getResponse().getContentAsString());
        studentIdInserted = (int) studentJSON.get(STUDENT_ID);
        student.setStudentId(studentIdInserted);

        Student studentRezultat =  studentRepository.findStudentById(studentIdInserted);
        Assert.isTrue(studentJSON.get(STUDENT_NAME).equals(studentRezultat.getStudentName()),
                "Nu a putut fi obtinut student in addStudentTest");
    }

    @Test
    @Order(2)
    public void addStudentTestBindingError() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(StudentExporter.exportStudent(student));
        dataToPost.remove("studentName");
        mockMvc.perform(post("/student/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));

    }

    @Test
    @Order(3)
    public void addStudentAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(StudentExporter.exportStudent(student, true));
        mockMvc.perform(post("/student/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(4)
    public void getStudentByIdTest() throws Exception{
        MvcResult studentResult =
                mockMvc.perform(get("/student/" + String.valueOf(studentIdInserted)))
                        .andReturn();
        int responseStatus = studentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul getStudentById");

        JSONObject studentJSON = new JSONObject(studentResult.getResponse().getContentAsString());
        Assert.isTrue(studentJSON.get(STUDENT_NAME).equals(studentName),
                "Nu a putut fi obtinut student in getStudentByIdTest");
    }

    @Test
    @Order(5)
    public void getStudentByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/student/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(6)
    public void updateStudentTest() throws Exception{
        student.setStudentName(studentName + " updated");
        student.setStudentAddress(studentAddress + " updated");
        student.setStudentSpecialty(studentSpecialty + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(StudentExporter.exportStudent(student, true));
        MvcResult studentResult =
                mockMvc.perform(put("/student/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = studentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul updateStudent");

        Student studentRezultat =  studentRepository.findStudentById(studentIdInserted);
        Assert.isTrue(studentRezultat.getStudentName().equals(student.getStudentName()) &&
                        studentRezultat.getStudentSpecialty().equals(student.getStudentSpecialty()) &&
                        studentRezultat.getStudentAddress().equals(student.getStudentAddress()),
                "Nu a putut fi obtinut student in updateStudentTest");
    }

    @Test
    @Order(7)
    public void retrieveStudentsTest() throws Exception{
        MvcResult studentResult =
                mockMvc.perform(get("/student/all/"))
                        .andReturn();
        int responseStatus = studentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul retrieveStudents");

        JSONArray studentJSON = new JSONArray(studentResult.getResponse().getContentAsString());
        boolean studentFound = false;
        for(int i = 0; i < studentJSON.length(); i++){
            if((Integer)((JSONObject)studentJSON.get(i)).get(STUDENT_ID) == studentIdInserted){
                studentFound = true;
            }
        }

        Assert.isTrue(studentFound,
                "Nu a putut fi obtinut student in retrieveStudentsTest");
    }

    @Test
    @Order(8)
    public void deleteStudentByIdTest() throws Exception{
        MvcResult studentResult =
                mockMvc.perform(delete("/student/" + String.valueOf(studentIdInserted)))
                        .andReturn();
        int responseStatus = studentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(),
                "Nu a putut fi executat cu succes request-ul deleteStudentById");

        Student studentRezultat =  studentRepository.findStudentById(studentIdInserted);
        Assert.isTrue(studentRezultat == null,
                "A fost gasit student dupa deleteStudentByIdTest");
    }
}
