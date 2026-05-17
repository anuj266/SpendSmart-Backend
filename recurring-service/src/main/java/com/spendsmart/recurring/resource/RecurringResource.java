package com.spendsmart.recurring.resource;



import com.spendsmart.recurring.entity.RecurringTransaction;
import com.spendsmart.recurring.service.RecurringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringResource {

    private final RecurringService service;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody RecurringTransaction rt) {
        return ResponseEntity.ok(service.addRecurring(rt));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<RecurringTransaction>> getByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransaction> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<List<RecurringTransaction>> active(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getActiveRecurring(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransaction> update(@PathVariable Long id, @RequestBody RecurringTransaction rt) {
        return ResponseEntity.ok(service.updateRecurring(id, rt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteRecurring(id);
        return ResponseEntity.ok("Deleted");
    }
}