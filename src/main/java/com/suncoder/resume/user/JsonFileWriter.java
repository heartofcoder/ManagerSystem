package com.suncoder.resume.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class JsonFileWriter {

    public void writeToJsonFile(List<UserResource> userResources){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            objectMapper.writeValue(new File("userResources.json"), userResources);
        }catch (Exception e){
            System.out.println("fail to write json file");
        }
    }
}