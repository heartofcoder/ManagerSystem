package com.suncoder.resume.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileReader {
    public List<UserResource> readJsonFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File("userResources.json");
            List<UserResource> userResources = objectMapper.readValue(file, new TypeReference<List<UserResource>>() {});
            return userResources;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
