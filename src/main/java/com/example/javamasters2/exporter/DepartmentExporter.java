package com.example.javamasters2.exporter;

import com.example.javamasters2.model.College;
import com.example.javamasters2.model.Department;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;

public class DepartmentExporter {

    public static Map<String, Object> exportDepartment(Department department, boolean withDepartmentId){
        Map<String, Object> exportData = new HashMap<>();
        if(withDepartmentId && department.getDepartmentId() != null){
            exportData.put(DEPARTMENT_ID, department.getDepartmentId());
        }
        exportData.put(DEPARTMENT_NAME, department.getDepartmentName());
        Map<String, Object> collegeData = CollegeExporter.exportCollege(department.getCollege(), true);
        exportData.put(COLLEGE, collegeData);

        return exportData;
    }
    public static Map<String, Object> exportDepartment(Department department){
        return exportDepartment(department, false);
    }
}
