package com.spendsmart.income.controller;

import com.spendsmart.income.entity.Income;
import com.spendsmart.income.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeResource {

    @Autowired
    private IncomeService service;

    // ✅ POST - Add Income
    @PostMapping
    public ResponseEntity<Income> add(@RequestBody Income income) {
        return ResponseEntity.ok(service.addIncome(income));
    }

    // ✅ GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Income> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getIncomeById(id));
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Income>> search(@PathVariable Long userId,
                                               @RequestParam String keyword) {
        return ResponseEntity.ok(service.searchIncomes(userId, keyword));
    }

    // ✅ GET by User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Income>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getIncomesByUser(userId));
    }

    // ✅ GET by Source
    @GetMapping("/source")
    public ResponseEntity<List<Income>> getBySource(
            @RequestParam Long userId,
            @RequestParam String source) {
        return ResponseEntity.ok(service.getIncomesBySource(userId, source));
    }

    // ✅ GET by Date Range
    @GetMapping("/range")
    public ResponseEntity<List<Income>> getByDateRange(
            @RequestParam Long userId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(service.getIncomesByDateRange(
                userId,
                LocalDate.parse(start),
                LocalDate.parse(end)));
    }

    // ✅ GET by Month (NEW - was missing)
    @GetMapping("/month")
    public ResponseEntity<List<Income>> getByMonth(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getIncomesByMonth(userId, month, year));
    }

    // ✅ GET Recurring Incomes (NEW - was missing)
    @GetMapping("/recurring")
    public ResponseEntity<List<Income>> getRecurring() {
        return ResponseEntity.ok(service.getRecurringIncomes());
    }

    // ✅ GET Total by User
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getTotalIncomeByUser(userId));
    }

    // ✅ GET Total by Month (NEW - was missing)
    @GetMapping("/total/month")
    public ResponseEntity<Double> getTotalByMonth(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getTotalIncomeByMonth(userId, month, year));
    }

    // ✅ PUT - Update Income
    @PutMapping("/{id}")
    public ResponseEntity<Income> update(@PathVariable Long id, @RequestBody Income income) {
        return ResponseEntity.ok(service.updateIncome(id, income));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteIncome(id);
        return ResponseEntity.ok("Income deleted successfully with id: " + id);
    }
}