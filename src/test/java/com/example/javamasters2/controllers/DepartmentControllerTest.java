package com.example.javamasters2.controllers;

import com.example.javamasters2.exporter.CollegeExporter;
import com.example.javamasters2.exporter.DepartmentExporter;
import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.repository.DepartmentRepository;
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
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private DepartmentRepository departmentRepository;
    private int departmentIdInserted;
    private String activeProfile;
    private College college;
    private Department department;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;

    private Integer collegeIdInserted;
    private String departmentName;

    @BeforeAll
    public void setup(){
        activeProfile = this.environment.getActiveProfiles()[0];
        college = new College("Facultatea testDepartment " + activeProfile, "Str. Department 8");
        try {
            Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college));
            MvcResult collegeResult =
                    mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPost).toString()))
                            .andReturn();

            int responseStatus = collegeResult.getResponse().getStatus();
            if(responseStatus == HttpStatus.OK.value()) {
                JSONObject collegeJSON = new JSONObject(collegeResult.getResponse().getContentAsString());
                collegeIdInserted = (int) collegeJSON.get(COLLEGE_ID);
                college.setCollegeId(collegeIdInserted);
            }
        } catch (Exception ex){
            System.out.println("Exception setting up department controller test");
        }
    }

    @Test
    @Order(1)
    public void addDepartmentTest() throws Exception{
        departmentName = "Departament test " + activeProfile;
        department = new Department(departmentName);
        department.setCollege(college);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department));
        MvcResult departmentResult =
                mockMvc.perform(post("/department/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = departmentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul addDepartment");

        JSONObject departmentJSON = new JSONObject(departmentResult.getResponse().getContentAsString());
        departmentIdInserted = (int) departmentJSON.get(DEPARTMENT_ID);
        department.setDepartmentId(departmentIdInserted);

        Department departmentRezultat =  departmentRepository.findDepartmentById(departmentIdInserted);
        Assert.isTrue(departmentJSON.get(DEPARTMENT_NAME).equals(departmentRezultat.getDepartmentName()), "Nu a putut fi obtinut department in addDepartmentTest");
    }

    @Test
    @Order(2)
    public void addDepartmentTestBindingError() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department));
        dataToPost.remove("departmentName");
        mockMvc.perform(post("/department/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));

    }

    @Test
    @Order(3)
    public void addDepartmentAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department, true));
        mockMvc.perform(post("/department/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(4)
    public void getDepartmentByIdTest() throws Exception{
        MvcResult departmentResult =
                mockMvc.perform(get("/department/" + String.valueOf(departmentIdInserted)))
                        .andReturn();

        int responseStatus = departmentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul getDepartmentById");

        JSONObject departmentJSON = new JSONObject(departmentResult.getResponse().getContentAsString());
        Assert.isTrue(departmentJSON.get(DEPARTMENT_NAME).equals(departmentName),
                "Nu a putut fi obtinut department in getDepartmentByIdTest");
    }

    @Test
    @Order(5)
    public void getDepartmentByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/department/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(6)
    public void updateDepartmentTest() throws Exception{
        department.setDepartmentName(departmentName + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department, true));
        MvcResult departmentResult =
                mockMvc.perform(put("/department/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = departmentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul updateDepartment");

        Department departmentRezultat =  departmentRepository.findDepartmentById(departmentIdInserted);
        Assert.isTrue(department.getDepartmentName().equals(departmentRezultat.getDepartmentName()),
                "Nu a putut fi obtinut department updated in updateDepartmentTest");
    }

    @Test
    @Order(7)
    public void retrieveDepartmentsTest() throws Exception{
        MvcResult departmentResult =
                mockMvc.perform(get("/department/all/"))
                        .andReturn();
        int responseStatus = departmentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul retrieveDepartments");

        JSONArray departmentJSON = new JSONArray(departmentResult.getResponse().getContentAsString());
        Assert.isTrue((Integer)((JSONObject)departmentJSON.get(departmentJSON.length() - 1)).get(DEPARTMENT_ID) == departmentIdInserted,
                "Nu a putut fi department department in retrieveDepartmentsTest");
    }

    @Test
    @Order(8)
    public void deleteDepartmentByIdTest() throws Exception{
        MvcResult departmentResult =
                mockMvc.perform(delete("/department/" + String.valueOf(departmentIdInserted)))
                        .andReturn();
        int responseStatus = departmentResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul deleteDepartmentById");

        Department departmentRezultat =  departmentRepository.findDepartmentById(departmentIdInserted);
        Assert.isTrue(departmentRezultat == null,
                "A fost gasit department dupa deleteDepartmentByIdTest");
    }

}
