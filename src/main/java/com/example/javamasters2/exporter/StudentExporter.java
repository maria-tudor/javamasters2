package com.example.javamasters2.exporter;

import com.example.javamasters2.model.Student;
import com.example.javamasters2.model.Subject;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;

public class StudentExporter {
    public static Map<String, Object> exportStudent(Student student, boolean withStudentId){
        Map<String, Object> exportData = new HashMap<>();
        if(withStudentId && student.getStudentId() != null){
            exportData.put(STUDENT_ID, student.getStudentId());
        }
        exportData.put(STUDENT_NAME, student.getStudentName());
        exportData.put(STUDENT_ADDRESS, student.getStudentAddress());
        exportData.put(STUDENT_SPECIALTY, student.getStudentSpecialty());

        return exportData;
    }
    public static Map<String, Object> exportStudent(Student student){
        return exportStudent(student, false);
    }
}
