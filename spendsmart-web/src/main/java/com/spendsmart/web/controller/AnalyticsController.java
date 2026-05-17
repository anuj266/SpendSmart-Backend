package com.spendsmart.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ FIX
import org.springframework.web.bind.annotation.*;

import com.spendsmart.web.client.AnalyticsClient;
import com.spendsmart.web.client.BudgetClient;
import com.spendsmart.web.client.RecurringClient;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsClient analyticsClient;
    private final BudgetClient budgetClient;
    private final RecurringClient recurringClient;

    @GetMapping("/charts")
    public String viewCharts(Model model) {
        model.addAttribute("data", analyticsClient.getMonthlySummary(1));
        return "analytics/charts";
    }

    @GetMapping("/budgets")
    public String viewBudgets(Model model) {
        model.addAttribute("budgets", budgetClient.getBudgets(1));
        return "analytics/budgets";
    }

    @PostMapping("/budget/add")
    public String addBudget(@ModelAttribute Object budget) {
        budgetClient.addBudget(budget);
        return "redirect:/analytics/budgets";
    }

    @GetMapping("/recurring")
    public String viewRecurring(Model model) {
        model.addAttribute("recurring", recurringClient.getRecurring(1));
        return "analytics/recurring";
    }

    @PostMapping("/recurring/add")
    public String addRecurring(@ModelAttribute Object rec) {
        recurringClient.addRecurring(rec);
        return "redirect:/analytics/recurring";
    }
}