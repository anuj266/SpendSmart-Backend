package com.spendsmart.expense.service;

import com.spendsmart.expense.config.RabbitMQConfig;
import com.spendsmart.expense.dto.NotificationEvent;
import com.spendsmart.expense.client.BudgetClient;
import com.spendsmart.expense.entity.Expense;
import com.spendsmart.expense.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetClient budgetClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────
    @Override
    public Expense addExpense(Expense expense) {
        Expense savedExpense = expenseRepository.save(expense);

        // Sync budget
        syncBudgetSpent(savedExpense.getUserId(), savedExpense.getCategoryId(), savedExpense.getAmount());

        // Publish to RabbitMQ
        publishExpenseEvent(
                savedExpense.getUserId(),
                "EXPENSE_CREATED",
                "INFO",
                "New expense added",
                "₹" + savedExpense.getAmount() + " - " + savedExpense.getTitle(),
                savedExpense.getExpenseId(),
                "notification.expense.created"
        );

        return savedExpense;
    }

    // ─────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────
    @Override
    public Optional<Expense> getExpenseById(Long expenseId) {
        return expenseRepository.findByExpenseId(expenseId);
    }

    @Override
    public List<Expense> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public List<Expense> getExpensesByCategory(Long userId, Long categoryId) {
        return expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

    @Override
    public List<Expense> getExpensesByDateRange(Long userId, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    @Override
    public List<Expense> getExpensesByMonth(Long userId, int month, int year) {
        return expenseRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    @Override
    public List<Expense> getExpensesByType(Long userId, String type) {
        return expenseRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<Expense> searchExpenses(Long userId, String keyword) {
        return expenseRepository.searchByKeyword(userId, keyword);
    }

    @Override
    public Double getTotalByUser(Long userId) {
        return expenseRepository.sumAmountByUserId(userId);
    }

    @Override
    public Double getTotalByCategory(Long userId, Long categoryId) {
        return expenseRepository.sumAmountByUserIdAndCategoryId(userId, categoryId);
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────
    @Override
    public Expense updateExpense(Long expenseId, Expense updatedExpense) {
        Expense existing = expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        Long previousCategoryId = existing.getCategoryId();
        double previousAmount   = existing.getAmount();

        if (updatedExpense.getTitle() != null)         existing.setTitle(updatedExpense.getTitle());
        if (updatedExpense.getAmount() > 0)            existing.setAmount(updatedExpense.getAmount());
        if (updatedExpense.getCurrency() != null)      existing.setCurrency(updatedExpense.getCurrency());
        if (updatedExpense.getType() != null)          existing.setType(updatedExpense.getType());
        if (updatedExpense.getPaymentMethod() != null) existing.setPaymentMethod(updatedExpense.getPaymentMethod());
        if (updatedExpense.getDate() != null)          existing.setDate(updatedExpense.getDate());
        if (updatedExpense.getNotes() != null)         existing.setNotes(updatedExpense.getNotes());
        if (updatedExpense.getReceiptUrl() != null)    existing.setReceiptUrl(updatedExpense.getReceiptUrl());
        existing.setRecurring(updatedExpense.isRecurring());
        if (updatedExpense.getCategoryId() != null)    existing.setCategoryId(updatedExpense.getCategoryId());

        Expense savedExpense = expenseRepository.save(existing);

        // Sync budget
        if (previousCategoryId != null && previousCategoryId.equals(savedExpense.getCategoryId())) {
            double delta = savedExpense.getAmount() - previousAmount;
            syncBudgetSpent(savedExpense.getUserId(), savedExpense.getCategoryId(), delta);
        } else {
            syncBudgetSpent(savedExpense.getUserId(), previousCategoryId, -previousAmount);
            syncBudgetSpent(savedExpense.getUserId(), savedExpense.getCategoryId(), savedExpense.getAmount());
        }

        // Publish to RabbitMQ
        publishExpenseEvent(
                savedExpense.getUserId(),
                "EXPENSE_UPDATED",
                "INFO",
                "Expense updated",
                "₹" + savedExpense.getAmount() + " - " + savedExpense.getTitle(),
                savedExpense.getExpenseId(),
                "notification.expense.updated"
        );

        return savedExpense;
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void deleteExpense(Long expenseId) {
        Expense existing = expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        // Sync budget (reverse the amount)
        syncBudgetSpent(existing.getUserId(), existing.getCategoryId(), -existing.getAmount());

        // Publish to RabbitMQ
        publishExpenseEvent(
                existing.getUserId(),
                "EXPENSE_DELETED",
                "INFO",
                "Expense deleted",
                "₹" + existing.getAmount() + " - " + existing.getTitle() + " was removed",
                existing.getExpenseId(),
                "notification.expense.deleted"
        );

        expenseRepository.deleteByExpenseId(expenseId);
    }

    // ─────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────

    private void syncBudgetSpent(Long userId, Long categoryId, double amountDelta) {
        budgetClient.updateSpentAmount(userId, categoryId, amountDelta);
    }

    private void publishExpenseEvent(Long userId, String type, String severity,
                                     String title, String message,
                                     Long relatedId, String routingKey) {
        try {
            NotificationEvent event = new NotificationEvent(
                    userId, type, severity,
                    title, message,
                    relatedId, "EXPENSE"
            );
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    routingKey,
                    event
            );
            log.info("Event [{}] published to RabbitMQ for user {}", type, userId);
        } catch (Exception e) {
            // Never let RabbitMQ failure break the expense operation
            log.error("Failed to publish event [{}] to RabbitMQ: {}", type, e.getMessage());
        }
    }
}