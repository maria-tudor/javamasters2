package com.example.javamasters2.controllers;

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

import javax.validation.ConstraintViolationException;
import java.util.Map;

import static com.example.javamasters2.constants.ExceptionConstants.*;
import static com.example.javamasters2.constants.ModelConstants.COLLEGE_ID;
import static com.example.javamasters2.constants.ModelConstants.COLLEGE_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CollegeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private CollegeRepository collegeRepository;
    private int collegeIdInserted;
    private String activeProfile;
    private College college;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;

    @BeforeAll
    public void setup(){
        activeProfile = this.environment.getActiveProfiles()[0];
    }

    @Test
    @Order(1)
    public void addCollegeTest() throws Exception{
        college = new College("Facultatea de Geografie " + activeProfile, "Str. Marinarilor 7");
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college));
        MvcResult collegeResult =
                mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = collegeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul addCollege");

        JSONObject collegeJSON = new JSONObject(collegeResult.getResponse().getContentAsString());
        collegeIdInserted = (int) collegeJSON.get(COLLEGE_ID);
        college.setCollegeId(collegeIdInserted);

        College collegeRezultat =  collegeRepository.findCollegeById(collegeIdInserted);
        Assert.isTrue(collegeJSON.get(COLLEGE_NAME).equals(collegeRezultat.getCollegeName()), "Nu a putut fi obtinut college in addCollegeTest");
    }

    @Test
    @Order(2)
    public void addCollegeBindingErrorTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college));
        dataToPost.remove("collegeName");
        mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));

    }

    @Test
    @Order(3)
    public void addCollegeAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college, true));
        mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(4)
    public void getCollegeByIdTest() throws Exception{
        MvcResult collegeResult =
                mockMvc.perform(get("/college/" + String.valueOf(collegeIdInserted)))
                .andReturn();
        int responseStatus = collegeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul getCollegeById");

        JSONObject collegeJSON = new JSONObject(collegeResult.getResponse().getContentAsString());
        Assert.isTrue(collegeJSON.get(COLLEGE_NAME).equals("Facultatea de Geografie" + " " + activeProfile),
                "Nu a putut fi obtinut college in getCollegeByIdTest");
    }

    @Test
    @Order(5)
    public void getCollegeByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/college/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(6)
    public void updateCollegeTest() throws Exception{
        college.setCollegeName(college.getCollegeName() + " updated");
        college.setCollegeAddress(college.getCollegeAddress() + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college, true));
        MvcResult collegeResult =
                mockMvc.perform(put("/college/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = collegeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul updateCollege");

        College collegeRezultat =  collegeRepository.findCollegeById(collegeIdInserted);
        Assert.isTrue(college.getCollegeName().equals(collegeRezultat.getCollegeName()) &&
                college.getCollegeAddress().equals(collegeRezultat.getCollegeAddress()),
                "Nu a putut fi obtinut college in updateCollegeTest");
    }

    @Test
    @Order(7)
    public void retrieveCollegesTest() throws Exception{
        MvcResult collegeResult =
                mockMvc.perform(get("/college/all/"))
                        .andReturn();
        int responseStatus = collegeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul getCollegeById");

        JSONArray collegeJSON = new JSONArray(collegeResult.getResponse().getContentAsString());
        Assert.isTrue((Integer)((JSONObject)collegeJSON.get(collegeJSON.length() - 1)).get(COLLEGE_ID) == collegeIdInserted,
                "Nu a putut fi obtinut college in getCollegeByIdTest");
    }

    @Test
    @Order(8)
    public void deleteCollegeByIdTest() throws Exception{
        MvcResult collegeResult =
                mockMvc.perform(delete("/college/" + String.valueOf(collegeIdInserted)))
                        .andReturn();
        int responseStatus = collegeResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul deleteCollegeById");

        College collegeRezultat =  collegeRepository.findCollegeById(collegeIdInserted);
        Assert.isTrue(collegeRezultat == null,
                "A fost gasit college dupa deleteCollegeByIdTest");
    }
}
