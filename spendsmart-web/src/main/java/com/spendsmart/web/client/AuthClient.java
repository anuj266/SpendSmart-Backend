package com.spendsmart.web.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8081/api/auth";

    // Register user
    public Object register(Object user) {
        return restTemplate.postForObject(BASE_URL + "/register", user, Object.class);
    }

    // Login user
    public Object login(Object loginRequest) {
        return restTemplate.postForObject(BASE_URL + "/login", loginRequest, Object.class);
    }

    // Get user profile
    public Object getProfile(int userId) {
        return restTemplate.getForObject(BASE_URL + "/profile/" + userId, Object.class);
    }

    // Update profile
    public void updateProfile(int userId, Object user) {
        restTemplate.put(BASE_URL + "/profile/" + userId, user);
    }
}