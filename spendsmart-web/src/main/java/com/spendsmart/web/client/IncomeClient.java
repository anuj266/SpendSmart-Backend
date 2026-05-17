package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IncomeClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8083/api/incomes";

    public List<Object> getAllIncomes(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/user/" + userId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching incomes for userId: {}", userId, e);
            return Collections.emptyList();
        }
    }

    public void addIncome(Object income) {
        try {
            restTemplate.postForObject(BASE_URL, income, Object.class);
        } catch (RestClientException e) {
            log.error("Error adding income", e);
            throw new RuntimeException("Failed to add income: " + e.getMessage());
        }
    }

    public void deleteIncome(int id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            log.error("Error deleting income with id: {}", id, e);
            throw new RuntimeException("Failed to delete income: " + e.getMessage());
        }
    }
}