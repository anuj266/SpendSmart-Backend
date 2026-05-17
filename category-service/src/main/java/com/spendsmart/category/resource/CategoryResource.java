package com.spendsmart.category.resource;

import com.spendsmart.category.entity.Category;
import com.spendsmart.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing category management endpoints under /categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    // field-level injection kept intentionally private (matches class diagram)
    @Autowired
    private CategoryService categoryService;

    // ─── Constructor ─────────────────────────────────────────────────────────

    public CategoryResource() {}

    // ─── Endpoints ───────────────────────────────────────────────────────────

    /**
     * POST /categories
     * Create a new category.
     */
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /categories/user/{userId}
     * Get all categories for a user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Category>> getByUser(@PathVariable Long userId) {
        List<Category> categories = categoryService.getByUserId(userId);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /categories/{categoryId}
     * Get a single category by its ID.
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /categories/user/{userId}/type/{type}
     * Get categories for a user filtered by type (EXPENSE or INCOME).
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<Category>> getByType(
            @PathVariable Long userId,
            @PathVariable String type) {
        List<Category> categories = categoryService.getByUserAndType(userId, type);
        return ResponseEntity.ok(categories);
    }

    /**
     * PUT /categories/{categoryId}
     * Update a category's details.
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> update(
            @PathVariable Long categoryId,
            @RequestBody Category category) {
        Category updated = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /categories/{categoryId}
     * Delete a category.
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /categories/defaults
     * Get all system-seeded default categories.
     */
    @GetMapping("/defaults")
    public ResponseEntity<List<Category>> getDefaults() {
        List<Category> defaults = categoryService.getDefaultCategories();
        return ResponseEntity.ok(defaults);
    }

    /**
     * POST /categories/user/{userId}/defaults
     * Seed default categories for a newly registered user.
     */
    @PostMapping("/user/{userId}/defaults")
    public ResponseEntity<Void> initDefaults(@PathVariable Long userId) {
        categoryService.initDefaultCategories(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /categories/{categoryId}/budget
     * Set a budget limit on a category.
     * Query param: amount
     */
    @PutMapping("/{categoryId}/budget")
    public ResponseEntity<Void> setBudget(
            @PathVariable Long categoryId,
            @RequestParam double amount) {
        categoryService.setCategoryBudget(categoryId, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /categories/user/{userId}/count
     * Get total category count for a user.
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Integer> getCount(@PathVariable int userId) {
        int count = categoryService.getCategoryCount(userId);
        return ResponseEntity.ok(count);
    }
}
