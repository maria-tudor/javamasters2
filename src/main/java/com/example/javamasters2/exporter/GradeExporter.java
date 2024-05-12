package com.example.javamasters2.exporter;

import com.example.javamasters2.model.Grade;
import com.example.javamasters2.model.Student;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;
import static com.example.javamasters2.constants.ModelConstants.STUDENT_SPECIALTY;

public class GradeExporter {
    public static Map<String, Object> exportGrade(Grade grade, boolean withGradeId){
        Map<String, Object> exportData = new HashMap<>();
        if(withGradeId && grade.getGradeId() != null){
            exportData.put(GRADE_ID, grade.getGradeId());
        }
        exportData.put(GRADE_DATE, grade.getGradeDate().toString());
        exportData.put(GRADE_VALUE, grade.getGradeValue());
        exportData.put(OBSERVATIONS, grade.getObservations());

        Map<String, Object> studentData = StudentExporter.exportStudent(grade.getStudent(), true);
        exportData.put(STUDENT, studentData);

        Map<String, Object> subjectData = SubjectExporter.exportSubject(grade.getSubject(), true);
        exportData.put(SUBJECT, subjectData);

        return exportData;
    }
    public static Map<String, Object> exportGrade(Grade grade){
        return exportGrade(grade, false);
    }
}
