package com.example.javamasters2.exporter;

import com.example.javamasters2.model.Department;
import com.example.javamasters2.model.Professor;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;

public class ProfessorExporter {
    public static Map<String, Object> exportProfessor(Professor professor, boolean withProfessorId){
        Map<String, Object> exportData = new HashMap<>();
        if(withProfessorId && professor.getProfessorId() != null){
            exportData.put(PROFESSOR_ID, professor.getProfessorId());
        }
        exportData.put(PROFESSOR_NAME, professor.getProfessorName());
        exportData.put(PROFESSOR_ADDRESS, professor.getProfessorAddress());
        exportData.put(PROFESSOR_ROLE, professor.getProfessorRole());

        Map<String, Object> collegeData = CollegeExporter.exportCollege(professor.getCollege(), true);
        exportData.put(COLLEGE, collegeData);

        Map<String, Object> departmentData = DepartmentExporter.exportDepartment(professor.getDepartment(), true);
        exportData.put(DEPARTMENT, departmentData);

        return exportData;
    }
    public static Map<String, Object> exportProfessor(Professor professor){
        return exportProfessor(professor, false);
    }
}
