package com.spendsmart.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.spendsmart.web.client.BudgetClient;
import com.spendsmart.web.client.CategoryClient;
import com.spendsmart.web.client.ExpenseClient;
import com.spendsmart.web.client.IncomeClient;
import com.spendsmart.web.client.NotificationClient;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseClient expenseClient;
    private final IncomeClient incomeClient;
    private final CategoryClient categoryClient;
    private final BudgetClient budgetClient;
    private final NotificationClient notificationClient;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        model.addAttribute("expenses", expenseClient.getAllExpenses(1));
        return "dashboard";
    }

    @GetMapping("/expenses")
    public String viewExpenses(Model model) {
        model.addAttribute("expenses", expenseClient.getAllExpenses(1));
        return "expenses";
    }

    @PostMapping("/expense/add")
    public String addExpense(@ModelAttribute Object expense) {
        expenseClient.addExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable int id) {
        expenseClient.deleteExpense(id);
        return "redirect:/expenses";
    }

    @GetMapping("/incomes")
    public String viewIncomes(Model model) {
        model.addAttribute("incomes", incomeClient.getAllIncomes(1));
        return "incomes";
    }

    @PostMapping("/income/add")
    public String addIncome(@ModelAttribute Object income) {
        incomeClient.addIncome(income);
        return "redirect:/incomes";
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model) {
        model.addAttribute("notifications", notificationClient.getNotifications(1));
        return "notifications";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}