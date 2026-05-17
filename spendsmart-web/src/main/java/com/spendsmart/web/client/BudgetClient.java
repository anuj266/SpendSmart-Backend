package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BudgetClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8085/api/budgets";

    public List<Object> getBudgets(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/user/" + userId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching budgets for userId: {}", userId, e);
            return Collections.emptyList();
        }
    }

    public void addBudget(Object budget) {
        try {
            restTemplate.postForObject(BASE_URL, budget, Object.class);
        } catch (RestClientException e) {
            log.error("Error adding budget", e);
            throw new RuntimeException("Failed to add budget: " + e.getMessage());
        }
    }
}