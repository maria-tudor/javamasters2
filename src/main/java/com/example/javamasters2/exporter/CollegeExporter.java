package com.example.javamasters2.exporter;

import com.example.javamasters2.model.College;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;

public class CollegeExporter {

    public static Map<String, Object> exportCollege(College college){
        return exportCollege(college, false);
    }

    public static Map<String, Object> exportCollege(College college, boolean withCollegeId){
        Map<String, Object> exportData = new HashMap<>();
        if(withCollegeId &&  college.getCollegeId() != null) {
            exportData.put(COLLEGE_ID, college.getCollegeId());
        }
        exportData.put(COLLEGE_NAME, college.getCollegeName());
        exportData.put(COLLEGE_ADDRESS, college.getCollegeAddress());

        return exportData;
    }
}
