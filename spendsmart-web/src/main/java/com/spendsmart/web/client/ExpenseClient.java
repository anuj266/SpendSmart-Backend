package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8082/api/expenses";

    public List<Object> getAllExpenses(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/user/" + userId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching expenses for userId: {}", userId, e);
            return Collections.emptyList();
        }
    }

    public void addExpense(Object expense) {
        try {
            restTemplate.postForObject(BASE_URL, expense, Object.class);
        } catch (RestClientException e) {
            log.error("Error adding expense", e);
            throw new RuntimeException("Failed to add expense: " + e.getMessage());
        }
    }

    public void deleteExpense(int id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            log.error("Error deleting expense with id: {}", id, e);
            throw new RuntimeException("Failed to delete expense: " + e.getMessage());
        }
    }
}