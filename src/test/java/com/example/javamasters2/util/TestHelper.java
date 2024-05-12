package com.example.javamasters2.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.util.Map;

public class TestHelper {
    public static Map convertGsonToFasterXmlMapStyle(Map<String, Object> map) throws Exception {
        String json = new Gson().toJson(map);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }
}
