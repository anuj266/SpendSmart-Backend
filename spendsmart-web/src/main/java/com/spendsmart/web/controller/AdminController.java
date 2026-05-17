package com.spendsmart.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spendsmart.web.client.AnalyticsClient;
import com.spendsmart.web.client.NotificationClient;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AnalyticsClient analyticsClient;
    private final NotificationClient notificationClient;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("summary", analyticsClient.getPlatformAnalytics());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageAllUsers(Model model) {
        model.addAttribute("users", analyticsClient.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/expenses")
    public String viewAllExpenses(Model model) {
        model.addAttribute("expenses", analyticsClient.getAllExpenses());
        return "admin/expenses";
    }

    @GetMapping("/incomes")
    public String viewAllIncomes(Model model) {
        model.addAttribute("incomes", analyticsClient.getAllIncomes());
        return "admin/incomes";
    }

    @GetMapping("/analytics")
    public String viewPlatformAnalytics(Model model) {
        model.addAttribute("data", analyticsClient.getPlatformAnalytics());
        return "admin/analytics";
    }

    @GetMapping("/top-users")
    public String viewTopUsers(Model model) {
        model.addAttribute("users", analyticsClient.getTopUsers());
        return "admin/top-users";
    }

  

    @GetMapping("/reports")
    public String generateReport(Model model) {
        model.addAttribute("report", analyticsClient.generateReport());
        return "admin/report";
    }

    @GetMapping("/logs")
    public String viewAuditLogs(Model model) {
        model.addAttribute("logs", analyticsClient.getAuditLogs());
        return "admin/logs";
    }
}