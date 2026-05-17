package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecurringClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8087/api/recurring";

    public List<Object> getRecurring(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/user/" + userId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching recurring transactions for userId: {}", userId, e);
            return Collections.emptyList();
        }
    }

    public void addRecurring(Object rec) {
        try {
            restTemplate.postForObject(BASE_URL, rec, Object.class);
        } catch (RestClientException e) {
            log.error("Error adding recurring transaction", e);
            throw new RuntimeException("Failed to add recurring transaction: " + e.getMessage());
        }
    }

    public void deleteRecurring(int id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            log.error("Error deleting recurring with id: {}", id, e);
            throw new RuntimeException("Failed to delete recurring transaction: " + e.getMessage());
        }
    }
}