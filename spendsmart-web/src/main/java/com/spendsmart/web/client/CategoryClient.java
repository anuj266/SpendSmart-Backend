package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8084/api/categories";

    public List<Object> getAllCategories(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/user/" + userId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching categories for userId: {}", userId, e);
            return Collections.emptyList();
        }
    }

    public void addCategory(Object category) {
        try {
            restTemplate.postForObject(BASE_URL, category, Object.class);
        } catch (RestClientException e) {
            log.error("Error adding category", e);
            throw new RuntimeException("Failed to add category: " + e.getMessage());
        }
    }

    public void deleteCategory(int id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            log.error("Error deleting category with id: {}", id, e);
            throw new RuntimeException("Failed to delete category: " + e.getMessage());
        }
    }
}