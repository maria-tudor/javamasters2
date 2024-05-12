package com.example.javamasters2.controllers;

import com.example.javamasters2.exporter.CollegeExporter;
import com.example.javamasters2.exporter.DepartmentExporter;
import com.example.javamasters2.exporter.ProfessorExporter;
import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;
import com.example.javamasters2.model.Professor;
import com.example.javamasters2.repository.DepartmentRepository;
import com.example.javamasters2.repository.ProfessorRepository;
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
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    private int departmentIdInserted;
    private String activeProfile;
    private College college;
    private Department department;
    private Professor professor;
    private static final int NOT_FOUND_ID = Integer.MAX_VALUE;

    private Integer collegeIdInserted;
    private String departmentName;

    private Integer professorIdInserted;
    private String professorName;
    private String professorAddress;
    private String professorRole;

    @BeforeAll
    public void setup() {
        activeProfile = this.environment.getActiveProfiles()[0];
        college = new College("Facultatea testProfessor " + activeProfile, "Str. Professor 8");
        department = new Department("Departament testProfessor " + activeProfile);
        try {
            Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(CollegeExporter.exportCollege(college));
            MvcResult collegeResult =
                    mockMvc.perform(post("/college/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPost).toString()))
                            .andReturn();

            int responseStatus = collegeResult.getResponse().getStatus();
            if (responseStatus == HttpStatus.OK.value()) {
                JSONObject collegeJSON = new JSONObject(collegeResult.getResponse().getContentAsString());
                collegeIdInserted = (int) collegeJSON.get(COLLEGE_ID);
                college.setCollegeId(collegeIdInserted);
            }

            department.setCollege(college);
            Map dataToPostDepartment = TestHelper.convertGsonToFasterXmlMapStyle(DepartmentExporter.exportDepartment(department));
            MvcResult departmentResult =
                    mockMvc.perform(post("/department/").contentType(MediaType.APPLICATION_JSON)
                                    .content(new JSONObject(dataToPostDepartment).toString()))
                            .andReturn();

            int responseStatusDepartment = departmentResult.getResponse().getStatus();
            if (responseStatusDepartment == HttpStatus.OK.value()) {
                JSONObject departmentJSON = new JSONObject(departmentResult.getResponse().getContentAsString());
                departmentIdInserted = (int) departmentJSON.get(DEPARTMENT_ID);
                department.setDepartmentId(departmentIdInserted);
            }
        } catch (Exception ex) {
            System.out.println("Exception setting up professor controller test");
        }
    }

    @Test
    @Order(1)
    public void addProfessorTest() throws Exception{
        professorName = "Professor test " + activeProfile;
        professorAddress = "Adresa test profesor";
        professorRole = "DECAN";
        professor = new Professor(professorName, professorAddress, professorRole);
        professor.setCollege(college);
        professor.setDepartment(department);
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(ProfessorExporter.exportProfessor(professor));
        MvcResult professorResult =
                mockMvc.perform(post("/professor/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPost).toString()))
                        .andReturn();

        int responseStatus = professorResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul addProfessor");

        JSONObject professorJSON = new JSONObject(professorResult.getResponse().getContentAsString());
        professorIdInserted = (int) professorJSON.get(PROFESSOR_ID);
        professor.setProfessorId(professorIdInserted);

        Professor professorRezultat =  professorRepository.findProfessorById(professorIdInserted);
        Assert.isTrue(professorJSON.get(PROFESSOR_NAME).equals(professorRezultat.getProfessorName()),
                "Nu a putut fi obtinut professor in addProfessorTest");
    }

    @Test
    @Order(2)
    public void addProfessorTestBindingError() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(ProfessorExporter.exportProfessor(professor));
        dataToPost.remove("professorName");
        mockMvc.perform(post("/professor/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isExpectationFailed())
                .andExpect(view().name(BINDING_FAILED_VIEW));

    }

    @Test
    @Order(3)
    public void addProfessorAlreadyReportedTest() throws Exception{
        Map dataToPost = TestHelper.convertGsonToFasterXmlMapStyle(ProfessorExporter.exportProfessor(professor, true));
        mockMvc.perform(post("/professor/").contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(dataToPost).toString()))
                .andExpect(status().isAlreadyReported())
                .andExpect(view().name(ALREADY_REPORTED_VIEW));

    }

    @Test
    @Order(4)
    public void getProfessorByIdTest() throws Exception{
        MvcResult professorResult =
                mockMvc.perform(get("/professor/" + String.valueOf(professorIdInserted)))
                        .andReturn();

        int responseStatus = professorResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul getProfessorById");

        JSONObject professorJSON = new JSONObject(professorResult.getResponse().getContentAsString());
        Assert.isTrue(professorJSON.get(PROFESSOR_NAME).equals(professorName),
                "Nu a putut fi obtinut professor in getProfessorByIdTest");
    }

    @Test
    @Order(5)
    public void getProfessorByIdNotFoundTest() throws Exception{
        mockMvc.perform(get("/professor/" + String.valueOf(NOT_FOUND_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW));
    }

    @Test
    @Order(6)
    public void updateProfessorTest() throws Exception{
        professor.setProfessorName(professorName + " updated");
        professor.setProfessorAddress(professorAddress + " updated");
        professor.setProfessorRole(professorRole + " updated");
        Map dataToPut = TestHelper.convertGsonToFasterXmlMapStyle(ProfessorExporter.exportProfessor(professor, true));
        MvcResult professorResult =
                mockMvc.perform(put("/professor/").contentType(MediaType.APPLICATION_JSON)
                                .content(new JSONObject(dataToPut).toString()))
                        .andReturn();

        int responseStatus = professorResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul updateProfessor");

        Professor professorRezultat =  professorRepository.findProfessorById(professorIdInserted);
        Assert.isTrue(professor.getProfessorName().equals(professorRezultat.getProfessorName()) &&
                        professor.getProfessorRole().equals(professorRezultat.getProfessorRole()) &&
                        professor.getProfessorAddress().equals(professorRezultat.getProfessorAddress()),
                "Nu a putut fi obtinut professor updated in updateProfessorTest");
    }

    @Test
    @Order(7)
    public void retrieveProfessorsTest() throws Exception{
        MvcResult professorResult =
                mockMvc.perform(get("/professor/all/"))
                        .andReturn();
        int responseStatus = professorResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul retrieveProfessors");

        JSONArray professorJSON = new JSONArray(professorResult.getResponse().getContentAsString());
        Assert.isTrue((Integer)((JSONObject)professorJSON.get(professorJSON.length() - 1)).get(PROFESSOR_ID) == professorIdInserted,
                "Nu a putut fi department department in retrieveProfessorsTest");
    }

    @Test
    @Order(8)
    public void deleteProfessorByIdTest() throws Exception{
        MvcResult professorResult =
                mockMvc.perform(delete("/professor/" + String.valueOf(professorIdInserted)))
                        .andReturn();
        int responseStatus = professorResult.getResponse().getStatus();
        Assert.isTrue(responseStatus == HttpStatus.OK.value(), "Nu a putut fi executat cu succes request-ul deleteProfessorById");

        Professor professorRezultat =  professorRepository.findProfessorById(professorIdInserted);
        Assert.isTrue(professorRezultat == null,
                "A fost gasit department dupa deleteProfessorByIdTest");
    }
}
