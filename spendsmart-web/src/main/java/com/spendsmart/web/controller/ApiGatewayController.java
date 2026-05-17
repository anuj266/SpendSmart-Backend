package com.spendsmart.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * FIXES APPLIED:
 * 1. Categories routes: 8084 → 8086  (was pointing to analytics-service by mistake)
 * 2. Analytics routes:  8086 → 8084  (was pointing to category-service by mistake)
 * 3. Authorization header is now forwarded to all downstream services
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    // ── Helper: extract and forward Authorization header ──────────────────────
    private HttpHeaders forwardAuth(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String auth = request.getHeader("Authorization");
        if (auth != null && !auth.isEmpty()) {
            headers.set("Authorization", auth);
        }
        headers.set("Content-Type", "application/json");
        return headers;
    }

    // ==================== INCOME ROUTES ====================
    @GetMapping("/incomes/{id}")
    public ResponseEntity<?> getIncome(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes/" + id,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching income", e);
            return ResponseEntity.internalServerError().body("Error fetching income: " + e.getMessage());
        }
    }

    @GetMapping("/incomes/user/{userId}")
    public ResponseEntity<?> getIncomesByUser(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes/user/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching incomes for user", e);
            return ResponseEntity.internalServerError().body("Error fetching incomes: " + e.getMessage());
        }
    }

    @GetMapping("/incomes/user/{userId}/total")
    public ResponseEntity<?> getTotalIncome(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes/user/" + userId + "/total",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching total income", e);
            return ResponseEntity.internalServerError().body("Error fetching total: " + e.getMessage());
        }
    }

    @GetMapping("/incomes/range")
    public ResponseEntity<?> getIncomesByRange(@RequestParam Long userId,
                                               @RequestParam String start,
                                               @RequestParam String end,
                                               HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8083/api/incomes/range?userId=" + userId + "&start=" + start + "&end=" + end,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching incomes by range", e);
            return ResponseEntity.internalServerError().body("Error fetching incomes by range: " + e.getMessage());
        }
    }

    @GetMapping("/incomes/month")
    public ResponseEntity<?> getIncomesByMonth(@RequestParam Long userId,
                                               @RequestParam int month,
                                               @RequestParam int year,
                                               HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8083/api/incomes/month?userId=" + userId + "&month=" + month + "&year=" + year,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching incomes by month", e);
            return ResponseEntity.internalServerError().body("Error fetching incomes by month: " + e.getMessage());
        }
    }

    @GetMapping("/incomes/total/month")
    public ResponseEntity<?> getIncomeTotalByMonth(@RequestParam Long userId,
                                                   @RequestParam int month,
                                                   @RequestParam int year,
                                                   HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8083/api/incomes/total/month?userId=" + userId + "&month=" + month + "&year=" + year,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching income total by month", e);
            return ResponseEntity.internalServerError().body("Error fetching income total by month: " + e.getMessage());
        }
    }

    @PostMapping("/incomes")
    public ResponseEntity<?> createIncome(@RequestBody Object income, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes",
                    HttpMethod.POST, new HttpEntity<>(income, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error creating income", e);
            return ResponseEntity.internalServerError().body("Error creating income: " + e.getMessage());
        }
    }

    @PutMapping("/incomes/{id}")
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @RequestBody Object income, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes/" + id,
                    HttpMethod.PUT, new HttpEntity<>(income, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating income", e);
            return ResponseEntity.internalServerError().body("Error updating income: " + e.getMessage());
        }
    }

    @DeleteMapping("/incomes/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8083/api/incomes/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting income", e);
            return ResponseEntity.internalServerError().body("Error deleting income: " + e.getMessage());
        }
    }

    // ==================== EXPENSE ROUTES ====================
    @GetMapping("/expenses/{id}")
    public ResponseEntity<?> getExpense(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/" + id,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching expense", e);
            return ResponseEntity.internalServerError().body("Error fetching expense: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}")
    public ResponseEntity<?> getExpensesByUser(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/user/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching expenses for user", e);
            return ResponseEntity.internalServerError().body("Error fetching expenses: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}/category/{categoryId}")
    public ResponseEntity<?> getExpensesByCategory(@PathVariable Long userId, @PathVariable Long categoryId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/user/" + userId + "/category/" + categoryId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching expenses by category", e);
            return ResponseEntity.internalServerError().body("Error fetching expenses: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}/total")
    public ResponseEntity<?> getTotalExpense(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/user/" + userId + "/total",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching total expenses", e);
            return ResponseEntity.internalServerError().body("Error fetching total: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}/range")
    public ResponseEntity<?> getExpensesByRange(@PathVariable Long userId,
                                                @RequestParam String start,
                                                @RequestParam String end,
                                                HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8082/api/expenses/user/" + userId + "/range?start=" + start + "&end=" + end,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching expenses by range", e);
            return ResponseEntity.internalServerError().body("Error fetching expenses by range: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}/month")
    public ResponseEntity<?> getExpensesByMonth(@PathVariable Long userId,
                                                @RequestParam int month,
                                                @RequestParam int year,
                                                HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8082/api/expenses/user/" + userId + "/month?month=" + month + "&year=" + year,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching expenses by month", e);
            return ResponseEntity.internalServerError().body("Error fetching expenses by month: " + e.getMessage());
        }
    }

    @GetMapping("/expenses/user/{userId}/search")
    public ResponseEntity<?> searchExpenses(@PathVariable Long userId,
                                            @RequestParam String keyword,
                                            HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8082/api/expenses/user/" + userId + "/search?keyword=" + keyword,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error searching expenses", e);
            return ResponseEntity.internalServerError().body("Error searching expenses: " + e.getMessage());
        }
    }

    @PostMapping("/expenses")
    public ResponseEntity<?> createExpense(@RequestBody Object expense, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses",
                    HttpMethod.POST, new HttpEntity<>(expense, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error creating expense", e);
            return ResponseEntity.internalServerError().body("Error creating expense: " + e.getMessage());
        }
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Object expense, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/" + id,
                    HttpMethod.PUT, new HttpEntity<>(expense, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating expense", e);
            return ResponseEntity.internalServerError().body("Error updating expense: " + e.getMessage());
        }
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8082/api/expenses/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting expense", e);
            return ResponseEntity.internalServerError().body("Error deleting expense: " + e.getMessage());
        }
    }

    // ==================== BUDGET ROUTES ====================
    @GetMapping("/budgets/{id}")
    public ResponseEntity<?> getBudget(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/" + id,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching budget", e);
            return ResponseEntity.internalServerError().body("Error fetching budget: " + e.getMessage());
        }
    }

    @GetMapping("/budgets/user/{userId}")
    public ResponseEntity<?> getBudgetsByUser(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/user/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching budgets for user", e);
            return ResponseEntity.internalServerError().body("Error fetching budgets: " + e.getMessage());
        }
    }

    @GetMapping("/budgets/user/{userId}/active")
    public ResponseEntity<?> getActiveBudgets(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/user/" + userId + "/active",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching active budgets", e);
            return ResponseEntity.internalServerError().body("Error fetching budgets: " + e.getMessage());
        }
    }

    @GetMapping("/budgets/user/{userId}/category/{categoryId}")
    public ResponseEntity<?> getBudgetByCategory(@PathVariable Long userId, @PathVariable Long categoryId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/user/" + userId + "/category/" + categoryId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching budget by category", e);
            return ResponseEntity.internalServerError().body("Error fetching budget: " + e.getMessage());
        }
    }

    @GetMapping("/budgets/{id}/progress")
    public ResponseEntity<?> getBudgetProgress(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/" + id + "/progress",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching budget progress", e);
            return ResponseEntity.internalServerError().body("Error fetching progress: " + e.getMessage());
        }
    }

    @PostMapping("/budgets")
    public ResponseEntity<?> createBudget(@RequestBody Object budget, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets",
                    HttpMethod.POST, new HttpEntity<>(budget, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error creating budget", e);
            return ResponseEntity.internalServerError().body("Error creating budget: " + e.getMessage());
        }
    }

    @PutMapping("/budgets/{id}")
    public ResponseEntity<?> updateBudget(@PathVariable Long id, @RequestBody Object budget, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/" + id,
                    HttpMethod.PUT, new HttpEntity<>(budget, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating budget", e);
            return ResponseEntity.internalServerError().body("Error updating budget: " + e.getMessage());
        }
    }

    @DeleteMapping("/budgets/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting budget", e);
            return ResponseEntity.internalServerError().body("Error deleting budget: " + e.getMessage());
        }
    }

    @GetMapping("/budgets/user/{userId}/alerts")
    public ResponseEntity<?> getBudgetAlerts(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/user/" + userId + "/alerts",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching budget alerts", e);
            return ResponseEntity.internalServerError().body("Error fetching budget alerts: " + e.getMessage());
        }
    }

    @PostMapping("/budgets/{id}/reset")
    public ResponseEntity<?> resetBudget(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8085/api/budgets/" + id + "/reset",
                    HttpMethod.POST, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error resetting budget", e);
            return ResponseEntity.internalServerError().body("Error resetting budget: " + e.getMessage());
        }
    }

    @PatchMapping("/budgets/{id}/spent")
    public ResponseEntity<?> updateBudgetSpent(@PathVariable Long id, @RequestParam double amount, HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8085/api/budgets/" + id + "/spent?amount=" + amount,
                    HttpMethod.PATCH, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating budget spent amount", e);
            return ResponseEntity.internalServerError().body("Error updating budget spent amount: " + e.getMessage());
        }
    }

    // ==================== CATEGORY ROUTES ====================
    // FIX: Was pointing to 8084 (analytics). Corrected to 8086 (category-service).
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/" + categoryId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching category", e);
            return ResponseEntity.internalServerError().body("Error fetching category: " + e.getMessage());
        }
    }

    @GetMapping("/categories/user/{userId}")
    public ResponseEntity<?> getCategoriesByUser(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/user/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching categories for user", e);
            return ResponseEntity.internalServerError().body("Error fetching categories: " + e.getMessage());
        }
    }

    @GetMapping("/categories/user/{userId}/type/{type}")
    public ResponseEntity<?> getCategoriesByType(@PathVariable Long userId, @PathVariable String type, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/user/" + userId + "/type/" + type,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching categories by type", e);
            return ResponseEntity.internalServerError().body("Error fetching categories: " + e.getMessage());
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody Object category, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories",
                    HttpMethod.POST, new HttpEntity<>(category, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error creating category", e);
            return ResponseEntity.internalServerError().body("Error creating category: " + e.getMessage());
        }
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody Object category, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/" + categoryId,
                    HttpMethod.PUT, new HttpEntity<>(category, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating category", e);
            return ResponseEntity.internalServerError().body("Error updating category: " + e.getMessage());
        }
    }

    @PutMapping("/categories/{categoryId}/budget")
    public ResponseEntity<?> updateCategoryBudget(@PathVariable Long categoryId, @RequestParam double amount, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/" + categoryId + "/budget?amount=" + amount,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating category budget", e);
            return ResponseEntity.internalServerError().body("Error updating category budget: " + e.getMessage());
        }
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8086/api/categories/" + categoryId,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting category", e);
            return ResponseEntity.internalServerError().body("Error deleting category: " + e.getMessage());
        }
    }

    // ==================== RECURRING ROUTES ====================
    @GetMapping("/recurring/{id}")
    public ResponseEntity<?> getRecurring(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring/" + id,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching recurring", e);
            return ResponseEntity.internalServerError().body("Error fetching recurring: " + e.getMessage());
        }
    }

    @GetMapping("/recurring/user/{userId}")
    public ResponseEntity<?> getRecurringByUser(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring/user/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching recurring for user", e);
            return ResponseEntity.internalServerError().body("Error fetching recurring: " + e.getMessage());
        }
    }

    @GetMapping("/recurring/active/{userId}")
    public ResponseEntity<?> getActiveRecurring(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring/active/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching active recurring", e);
            return ResponseEntity.internalServerError().body("Error fetching recurring: " + e.getMessage());
        }
    }

    @PostMapping("/recurring")
    public ResponseEntity<?> createRecurring(@RequestBody Object recurring, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring",
                    HttpMethod.POST, new HttpEntity<>(recurring, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error creating recurring", e);
            return ResponseEntity.internalServerError().body("Error creating recurring: " + e.getMessage());
        }
    }

    @PutMapping("/recurring/{id}")
    public ResponseEntity<?> updateRecurring(@PathVariable Long id, @RequestBody Object recurring, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring/" + id,
                    HttpMethod.PUT, new HttpEntity<>(recurring, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating recurring", e);
            return ResponseEntity.internalServerError().body("Error updating recurring: " + e.getMessage());
        }
    }

    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<?> deleteRecurring(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8087/api/recurring/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting recurring", e);
            return ResponseEntity.internalServerError().body("Error deleting recurring: " + e.getMessage());
        }
    }

    // ==================== NOTIFICATION ROUTES ====================
    @GetMapping("/notifications/{recipientId}")
    public ResponseEntity<?> getNotifications(@PathVariable Long recipientId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/" + recipientId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching notifications", e);
            return ResponseEntity.internalServerError().body("Error fetching notifications: " + e.getMessage());
        }
    }

    @GetMapping("/notifications/unread-count/{recipientId}")
    public ResponseEntity<?> getUnreadCount(@PathVariable Long recipientId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/unread-count/" + recipientId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching unread count", e);
            return ResponseEntity.internalServerError().body("Error fetching count: " + e.getMessage());
        }
    }

    @PutMapping("/notifications/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/read/" + id,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error marking notification as read", e);
            return ResponseEntity.internalServerError().body("Error updating notification: " + e.getMessage());
        }
    }

    @PutMapping("/notifications/read/all/{recipientId}")
    public ResponseEntity<?> markAllNotificationsRead(@PathVariable Long recipientId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/read/all/" + recipientId,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error marking all notifications as read", e);
            return ResponseEntity.internalServerError().body("Error updating notifications: " + e.getMessage());
        }
    }

    @PutMapping("/notifications/ack/{id}")
    public ResponseEntity<?> acknowledgeNotification(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/ack/" + id,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error acknowledging notification", e);
            return ResponseEntity.internalServerError().body("Error acknowledging notification: " + e.getMessage());
        }
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deleting notification", e);
            return ResponseEntity.internalServerError().body("Error deleting notification: " + e.getMessage());
        }
    }

    @PostMapping("/notifications/bulk")
    public ResponseEntity<?> sendBulkNotification(@RequestBody Object userIds,
                                                  @RequestParam String title,
                                                  @RequestParam String message,
                                                  HttpServletRequest request) {
        try {
            return restTemplate.exchange(
                    "http://localhost:8088/api/notifications/bulk?title=" + title + "&message=" + message,
                    HttpMethod.POST, new HttpEntity<>(userIds, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error sending bulk notification", e);
            return ResponseEntity.internalServerError().body("Error sending bulk notification: " + e.getMessage());
        }
    }

    @GetMapping("/notifications/all")
    public ResponseEntity<?> getAllNotifications(HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8088/api/notifications/all",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching all notifications", e);
            return ResponseEntity.internalServerError().body("Error fetching all notifications: " + e.getMessage());
        }
    }

    // ==================== ANALYTICS ROUTES ====================
    // FIX: Was pointing to 8086 (category-service). Corrected to 8084 (analytics-service).
    @GetMapping("/analytics/monthly/{userId}")
    public ResponseEntity<?> getMonthlyAnalytics(@PathVariable Long userId,
                                                 @RequestParam(required = false) Integer year,
                                                 @RequestParam(required = false) Integer month,
                                                 HttpServletRequest request) {
        try {
            String url = "http://localhost:8084/api/analytics/monthly/" + userId;
            if (year != null) url += "?year=" + year;
            if (month != null) url += (year != null ? "&" : "?") + "month=" + month;
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching monthly analytics", e);
            return ResponseEntity.internalServerError().body("Error fetching analytics: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/yearly/{userId}")
    public ResponseEntity<?> getYearlyAnalytics(@PathVariable Long userId,
                                                @RequestParam(required = false) Integer year,
                                                HttpServletRequest request) {
        try {
            String url = "http://localhost:8084/api/analytics/yearly/" + userId;
            if (year != null) url += "?year=" + year;
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching yearly analytics", e);
            return ResponseEntity.internalServerError().body("Error fetching analytics: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/categories/{userId}")
    public ResponseEntity<?> getCategoriesAnalytics(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8084/api/analytics/categories/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching categories analytics", e);
            return ResponseEntity.internalServerError().body("Error fetching analytics: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/health/{userId}")
    public ResponseEntity<?> getHealthAnalytics(@PathVariable Long userId, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8084/api/analytics/health/" + userId,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching health analytics", e);
            return ResponseEntity.internalServerError().body("Error fetching analytics: " + e.getMessage());
        }
    }

    // ==================== AUTH ROUTES ====================
    @GetMapping("/auth/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/profile/" + id,
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching profile", e);
            return ResponseEntity.internalServerError().body("Error fetching profile: " + e.getMessage());
        }
    }
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Object requestBody, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/register",
                    HttpMethod.POST, new HttpEntity<>(requestBody, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error registering user", e);
            return ResponseEntity.internalServerError().body("Error registering: " + e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Object requestBody, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/login",
                    HttpMethod.POST, new HttpEntity<>(requestBody, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error logging in", e);
            return ResponseEntity.internalServerError().body("Error logging in: " + e.getMessage());
        }
    }

    @GetMapping("/auth/google/config")
    public ResponseEntity<?> getGoogleConfig(HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/google/config",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching Google OAuth config", e);
            return ResponseEntity.internalServerError().body("Error fetching Google OAuth config: " + e.getMessage());
        }
    }

    @PostMapping("/auth/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Object requestBody, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/login/google",
                    HttpMethod.POST, new HttpEntity<>(requestBody, forwardAuth(request)), Object.class);
        } catch (HttpStatusCodeException e) {
            log.error("Google login failed in auth-service", e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error logging in with Google", e);
            return ResponseEntity.internalServerError().body("Error logging in with Google: " + e.getMessage());
        }
    }

    // Income search
    @GetMapping("/incomes/user/{userId}/search")
    public ResponseEntity<?> searchIncomes(@PathVariable Long userId,
                                           @RequestParam String keyword,
                                           HttpServletRequest request) {
        return restTemplate.exchange(
                "http://localhost:8083/api/incomes/user/" + userId + "/search?keyword=" + keyword,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    // Analytics routes
    @GetMapping("/analytics/income-vs-expense/{userId}")
    public ResponseEntity<?> incomeVsExpense(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/income-vs-expense/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @GetMapping("/analytics/savings-rate/{userId}")
    public ResponseEntity<?> savingsRate(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/savings-rate/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @GetMapping("/analytics/top-categories/{userId}")
    public ResponseEntity<?> topCategories(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/top-categories/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @GetMapping("/analytics/daily-trend/{userId}")
    public ResponseEntity<?> dailyTrend(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/daily-trend/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @GetMapping("/analytics/cashflow/{userId}")
    public ResponseEntity<?> cashflow(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/cashflow/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @GetMapping("/analytics/forecast/{userId}")
    public ResponseEntity<?> forecast(@PathVariable Long userId, HttpServletRequest request) {
        return restTemplate.exchange("http://localhost:8084/api/analytics/forecast/" + userId,
                HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
    }

    @PutMapping("/auth/profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Object requestBody, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/profile/" + id,
                    HttpMethod.PUT, new HttpEntity<>(requestBody, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating profile", e);
            return ResponseEntity.internalServerError().body("Error updating profile: " + e.getMessage());
        }
    }

    @PutMapping("/auth/password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Object requestBody, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/password/" + id,
                    HttpMethod.PUT, new HttpEntity<>(requestBody, forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error changing password", e);
            return ResponseEntity.internalServerError().body("Error changing password: " + e.getMessage());
        }
    }

    @PutMapping("/auth/currency/{id}")
    public ResponseEntity<?> updateCurrency(@PathVariable Long id, @RequestParam String currency, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/currency/" + id + "?currency=" + currency,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating currency", e);
            return ResponseEntity.internalServerError().body("Error updating currency: " + e.getMessage());
        }
    }

    @PutMapping("/auth/budget/{id}")
    public ResponseEntity<?> updateMonthlyBudget(@PathVariable Long id, @RequestParam Double budget, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/budget/" + id + "?budget=" + budget,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error updating monthly budget", e);
            return ResponseEntity.internalServerError().body("Error updating monthly budget: " + e.getMessage());
        }
    }

    @DeleteMapping("/auth/deactivate/{id}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/deactivate/" + id,
                    HttpMethod.DELETE, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error deactivating account", e);
            return ResponseEntity.internalServerError().body("Error deactivating account: " + e.getMessage());
        }
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/users",
                    HttpMethod.GET, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.internalServerError().body("Error fetching users: " + e.getMessage());
        }
    }

    @PutMapping("/auth/promote/{id}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/promote/" + id,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error promoting user to admin", e);
            return ResponseEntity.internalServerError().body("Error promoting user: " + e.getMessage());
        }
    }

    @PutMapping("/auth/reactivate/{id}")
    public ResponseEntity<?> reactivateAccount(@PathVariable Long id, HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/reactivate/" + id,
                    HttpMethod.PUT, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error reactivating account", e);
            return ResponseEntity.internalServerError().body("Error reactivating account: " + e.getMessage());
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            return restTemplate.exchange("http://localhost:8081/api/auth/logout",
                    HttpMethod.POST, new HttpEntity<>(forwardAuth(request)), Object.class);
        } catch (RestClientException e) {
            log.error("Error logging out", e);
            return ResponseEntity.internalServerError().body("Error logging out: " + e.getMessage());
        }
    }
}