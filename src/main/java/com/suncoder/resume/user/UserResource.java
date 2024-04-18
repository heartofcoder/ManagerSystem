package com.suncoder.resume.user;

import java.util.List;

public class UserResource {

    private Long userId;

    private List<String> endpoint;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "UserResource{" +
                "userId=" + userId +
                ", endpoint=" + endpoint +
                '}';
    }
}
