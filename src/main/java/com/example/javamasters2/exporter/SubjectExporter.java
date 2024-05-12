package com.example.javamasters2.exporter;

import com.example.javamasters2.model.Professor;
import com.example.javamasters2.model.Subject;

import java.util.HashMap;
import java.util.Map;

import static com.example.javamasters2.constants.ModelConstants.*;

public class SubjectExporter {
    public static Map<String, Object> exportSubject(Subject subject, boolean withSubjectId){
        Map<String, Object> exportData = new HashMap<>();
        if(withSubjectId && subject.getSubjectId() != null){
            exportData.put(SUBJECT_ID, subject.getSubjectId());
        }
        exportData.put(SUBJECT_NAME, subject.getSubjectName());
        exportData.put(SUBJECT_DESCRIPTION, subject.getSubjectDescription());

        return exportData;
    }
    public static Map<String, Object> exportSubject(Subject subject){
        return exportSubject(subject, false);
    }
}
