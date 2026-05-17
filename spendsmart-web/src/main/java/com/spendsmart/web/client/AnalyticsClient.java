package com.spendsmart.web.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyticsClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8086/api/analytics";

    public Object getMonthlySummary(int userId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/monthly/" + userId, Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching monthly summary for userId: {}", userId, e);
            return new HashMap<>();
        }
    }

    public Object getPlatformAnalytics() {
        try {
            return restTemplate.getForObject(BASE_URL + "/platform", Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching platform analytics", e);
            return new HashMap<>();
        }
    }

    public List<Object> getAllUsers() {
        try {
            return restTemplate.getForObject(BASE_URL + "/users", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching all users", e);
            return Collections.emptyList();
        }
    }

    public List<Object> getAllExpenses() {
        try {
            return restTemplate.getForObject(BASE_URL + "/expenses", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching all expenses", e);
            return Collections.emptyList();
        }
    }

    public List<Object> getAllIncomes() {
        try {
            return restTemplate.getForObject(BASE_URL + "/incomes", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching all incomes", e);
            return Collections.emptyList();
        }
    }

    public List<Object> getTopUsers() {
        try {
            return restTemplate.getForObject(BASE_URL + "/top-users", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching top users", e);
            return Collections.emptyList();
        }
    }

    public Object generateReport() {
        try {
            return restTemplate.getForObject(BASE_URL + "/report", Object.class);
        } catch (RestClientException e) {
            log.error("Error generating report", e);
            return new HashMap<>();
        }
    }

    public List<Object> getAuditLogs() {
        try {
            return restTemplate.getForObject(BASE_URL + "/logs", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching audit logs", e);
            return Collections.emptyList();
        }
    }
}