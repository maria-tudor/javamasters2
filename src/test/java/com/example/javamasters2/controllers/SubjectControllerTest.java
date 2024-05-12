package com.example.javamasters2.controllers;

import com.example.javamasters2.exporter.CollegeExporter;
import com.example.javamasters2.exporter.SubjectExporter;
import com.example.javamasters2.model.Subject;
import com.example.javamasters2.repository.SubjectRepository;
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
import static com.example.javamasters2.constants.ModelConstants.SUBJECT_ID;
import static com.example.javamasters2.constants.ModelConstants.SUBJECT_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private SubjectRepository subjectRepository;
    private int subjectIdInserted;
    private String activeProfile;
    private Subject subject;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;
    private String subjectName;
    private String subjectDescription;

    @BeforeAll
    public void setup(){
        activeProfile = this.environment.getActiveProfiles()[0];
    }

    @Test
    @Order(1)
    public void addSubjectTest() throws Exception{
        subjectName = "Materie  " + activeProfile;
        subjectDescription = "materie test subject";
        subject = new Subject(subjectName, subjectDescription);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(SubjectExporter.exportSubject(subject));
        MvcResult subjectResult =
                mockMvc.perform(post("/subject/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = subjectResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul addSubject");

        JSONObject subjectJSON = new JSONObject(subjectResult.getResponse().getContentAsString());
        subjectIdInserted = (int) subjectJSON.get(SUBJECT_ID);
        subject.setSubjectId(subjectIdInserted);

        Subject subjectRezultat =  subjectRepository.findSubjectById(subjectIdInserted);
        Assert.isTrue(subjectJSON.get(SUBJECT_NAME).equals(subjectRezultat.getSubjectName()), "Nu a putut fi obtinut subject in addSubjectTest");
    }

    @Test
    @Order(2)
    public void addSubjectTestBindingError() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(SubjectExporter.exportSubject(subject));
        dataToPost.remove("subjectName");
        mockMvc.perform(post("/subject/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));

    }

    @Test
    @Order(3)
    public void addSubjectAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(SubjectExporter.exportSubject(subject, true));
        mockMvc.perform(post("/subject/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(4)
    public void getSubjectByIdTest() throws Exception{
        MvcResult subjectResult =
                mockMvc.perform(get("/subject/" + String.valueOf(subjectIdInserted)))
                        .andReturn();
        int responseStatus = subjectResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul getSubjectById");

        JSONObject subjectJSON = new JSONObject(subjectResult.getResponse().getContentAsString());
        Assert.isTrue(subjectJSON.get(SUBJECT_NAME).equals(subjectName),
                "Nu a putut fi obtinut subject in getSubjectByIdTest");
    }

    @Test
    @Order(5)
    public void getSubjectByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/subject/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(6)
    public void updateSubjectTest() throws Exception{
        subject.setSubjectName(subjectName + " updated");
        subject.setSubjectDescription(subjectDescription + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(SubjectExporter.exportSubject(subject, true));
        MvcResult subjectResult =
                mockMvc.perform(put("/subject/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = subjectResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul updateSubject");

        Subject subjectRezultat =  subjectRepository.findSubjectById(subjectIdInserted);
        Assert.isTrue(subjectRezultat.getSubjectName().equals(subject.getSubjectName()) &&
                        subjectRezultat.getSubjectDescription().equals(subject.getSubjectDescription()),
                "Nu a putut fi obtinut subject in updateSubjectTest");
    }

    @Test
    @Order(7)
    public void retrieveSubjectsTest() throws Exception{
        MvcResult subjectResult =
                mockMvc.perform(get("/subject/all/"))
                        .andReturn();
        int responseStatus = subjectResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul retrieveSubjects");

        JSONArray subjectJSON = new JSONArray(subjectResult.getResponse().getContentAsString());
        Assert.isTrue((Integer)((JSONObject)subjectJSON.get(subjectJSON.length() - 1)).get(SUBJECT_ID) == subjectIdInserted,
                "Nu a putut fi obtinut subject in retrieveSubjectsTest");
    }

    @Test
    @Order(8)
    public void deleteSubjectByIdTest() throws Exception{
        MvcResult subjectResult =
                mockMvc.perform(delete("/subject/" + String.valueOf(subjectIdInserted)))
                        .andReturn();
        int responseStatus = subjectResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul deleteSubjectById");

        Subject subjectRezultat =  subjectRepository.findSubjectById(subjectIdInserted);
        Assert.isTrue(subjectRezultat == null,
                "A fost gasit subject dupa deleteSubjectByIdTest");
    }
}
