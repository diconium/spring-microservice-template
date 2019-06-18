package com.diconium.microservice.util;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileParser {

    private static String BASE_DIR =
        System.getProperty("user.dir") + "/src/test/java/com/diconium/microservice/resources/";

    public static JSONObject parseJSONObject(String filepath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(BASE_DIR + filepath));
    }

    public static JSONArray parseJSONArray(String filepath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new FileReader(BASE_DIR + filepath));
    }

}
