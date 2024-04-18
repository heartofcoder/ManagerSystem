package com.suncoder.resume.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
public class UserController {

    @PostMapping("/admin/addUser")
    public String addUser(@RequestParam("userResource") String userResource,
                            @RequestHeader("user")String user) throws JsonProcessingException {

        //decode the header
        byte[] decodedBytes = Base64.getDecoder().decode(user);
        String decodedStirng = new String(decodedBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        User accessUser = objectMapper.readValue(decodedStirng, User.class);
        if (!accessUser.getRole().equals("admin")){
            return "no access to this endpoint";
        }

        UserResource userResourceObject = objectMapper.readValue(userResource,UserResource.class);
        JsonFileWriter jsonFileWriter = new JsonFileWriter();
        JsonFileReader jsonFileReader = new JsonFileReader();
        List<UserResource> userResourceList = jsonFileReader.readJsonFromFile();
        userResourceList.add(userResourceObject);
        jsonFileWriter.writeToJsonFile(userResourceList);
        return "success";
    }

    @PostMapping("/user/resource")
    public String verifyResource(@RequestParam("userId") Long userId,
                                 @RequestParam("resource") String resource,
                                 @RequestHeader("user")String user) throws JsonProcessingException {

        //decode the header
        byte[] decodedBytes = Base64.getDecoder().decode(user);
        String decodedStirng = new String(decodedBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        User accessUser = objectMapper.readValue(decodedStirng, User.class);//record the caller

        JsonFileReader jsonFileReader = new JsonFileReader();
        List<UserResource> userResourceList = jsonFileReader.readJsonFromFile();

        //verify the resouces access for users
        if(!CollectionUtils.isEmpty(userResourceList)) {
            for (UserResource userResource : userResourceList) {
                if (userResource.getUserId().longValue() == userId) {
                    List<String> resources = userResource.getEndpoint();
                    if (resources.contains(resource))
                        return "success";
                }
            }
        }

        return "failure";
    }
}
