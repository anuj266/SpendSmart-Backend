package com.spendsmart.auth.service;

import com.spendsmart.auth.entity.User;
import java.util.List;
import java.util.Optional;

public interface AuthService {

    // ── Core Auth ──────────────────────────────────────────────
    User register(User user);

    String login(String email, String password);

    void logout(String token);

    boolean validateToken(String token);

    String refreshToken(String token);

    // ── User Queries ───────────────────────────────────────────
    User getUserById(Long userId);

    User getUserByEmail(String email);

    Optional<User> findOptionalUserByEmail(String email);

    List<User> getAllUsers();

    // ── Profile Management ─────────────────────────────────────
    User updateProfile(Long userId, User updatedUser);

    void changePassword(Long userId, String currentPassword, String newPassword);

    void updateCurrency(Long userId, String currency);

    void updateMonthlyBudget(Long userId, Double monthlyBudget);

    void deactivateAccount(Long userId);

    void promoteToAdmin(Long userId);

    void reactivateAccount(Long userId);
}
