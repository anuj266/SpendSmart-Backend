package com.spendsmart.auth.controller;

import com.spendsmart.auth.config.JwtUtil;
import com.spendsmart.auth.dto.*;
import com.spendsmart.auth.entity.User;
import com.spendsmart.auth.service.AuthService;
import com.spendsmart.auth.service.GoogleOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AuthController — REST controller
 *
 * Endpoints:
 *   POST   /api/auth/register
 *   POST   /api/auth/login
 *   POST   /api/auth/login/google
 *   POST   /api/auth/logout
 *   POST   /api/auth/refresh
 *   GET    /api/auth/google/config
 *   GET    /api/auth/profile/{id}
 *   PUT    /api/auth/profile/{id}
 *   PUT    /api/auth/password/{id}
 *   PUT    /api/auth/currency/{id}
 *   PUT    /api/auth/budget/{id}
 *   DELETE /api/auth/deactivate/{id}
 *   PUT    /api/auth/reactivate/{id}
 *   PUT    /api/auth/promote/{id}
 *   GET    /api/auth/users
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    // ── Google OAuth Config ──────────────────────────────────────
    @GetMapping("/google/config")
    public ResponseEntity<GoogleOAuthConfigResponse> googleOAuthConfig() {
        return ResponseEntity.ok(new GoogleOAuthConfigResponse(googleOAuthService.getClientId()));
    }

    // ── Register ─────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .passwordHash(req.getPassword())
                .role("USER")
                .build();
        User saved = authService.register(user);
        String token = jwtUtil.generateToken(saved.getEmail());
        return ResponseEntity.ok(new AuthResponse(
                token,
                saved.getUserId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getRole()
        ));
    }

    // ── Login ─────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        User user = authService.getUserByEmail(req.getEmail());
        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        ));
    }

    // ── Google OAuth Login ────────────────────────────────────────
    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest req) {
        try {
            GoogleTokenResponse googleToken = googleOAuthService.exchangeCodeForToken(
                    req.getCode(),
                    req.getCodeVerifier(),
                    req.getRedirectUri()
            );

            User user = authService.findOptionalUserByEmail(googleToken.getEmail()).orElse(null);

            if (user == null) {
                user = User.builder()
                        .email(googleToken.getEmail())
                        .fullName(googleToken.getFullName())
                        .avatarUrl(googleToken.getPicture())
                        .provider(User.Provider.GOOGLE)
                        .isActive(true)
                        .role("USER")
                        .build();
                user = authService.register(user);
            } else if (user.getProvider() == null || user.getProvider() == User.Provider.LOCAL) {
                user.setProvider(User.Provider.GOOGLE);
                user.setAvatarUrl(googleToken.getPicture());
                user = authService.updateProfile(user.getUserId(), user);
            }

            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(
                    token,
                    user.getUserId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ── Logout ────────────────────────────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        authService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ── Refresh Token ─────────────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader("Authorization") String authHeader) {
        String oldToken = extractToken(authHeader);
        String newToken = authService.refreshToken(oldToken);
        return ResponseEntity.ok(Map.of("token", newToken));
    }

    // ── Profile GET ───────────────────────────────────────────────
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(toUserProfileResponse(authService.getUserById(id)));
    }

    // ── Profile PUT ───────────────────────────────────────────────
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable Long id,
                                                             @RequestBody UpdateProfileRequest req) {
        User patch = new User();
        patch.setFullName(req.getFullName());
        patch.setAvatarUrl(req.getAvatarUrl());
        patch.setBio(req.getBio());
        patch.setTimezone(req.getTimezone());
        return ResponseEntity.ok(toUserProfileResponse(authService.updateProfile(id, patch)));
    }

    // ── Change Password ───────────────────────────────────────────
    @PutMapping("/password/{id}")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable Long id,
                                                              @RequestBody ChangePasswordRequest req) {
        authService.changePassword(id, req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    // ── Currency ──────────────────────────────────────────────────
    @PutMapping("/currency/{id}")
    public ResponseEntity<Map<String, String>> updateCurrency(@PathVariable Long id,
                                                              @RequestParam String currency) {
        authService.updateCurrency(id, currency);
        return ResponseEntity.ok(Map.of("message", "Currency updated to " + currency.toUpperCase()));
    }

    // ── Monthly Budget ────────────────────────────────────────────
    @PutMapping("/budget/{id}")
    public ResponseEntity<Map<String, String>> updateBudget(@PathVariable Long id,
                                                            @RequestParam Double budget) {
        authService.updateMonthlyBudget(id, budget);
        return ResponseEntity.ok(Map.of("message", "Monthly budget updated"));
    }

    // ── Deactivate Account ────────────────────────────────────────
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Map<String, String>> deactivate(@PathVariable Long id) {
        authService.deactivateAccount(id);
        return ResponseEntity.ok(Map.of("message", "Account deactivated successfully"));
    }

    // ── Reactivate Account (Admin) ────────────────────────────────
    @PutMapping("/reactivate/{id}")
    public ResponseEntity<Map<String, String>> reactivate(@PathVariable Long id) {
        authService.reactivateAccount(id);
        return ResponseEntity.ok(Map.of("message", "Account reactivated successfully"));
    }

    // ── Promote to Admin ──────────────────────────────────────────
    @PutMapping("/promote/{id}")
    public ResponseEntity<Map<String, String>> promoteToAdmin(@PathVariable Long id) {
        authService.promoteToAdmin(id);
        return ResponseEntity.ok(Map.of("message", "User promoted to ADMIN successfully"));
    }

    // ── Get All Users (Admin) ─────────────────────────────────────
    @GetMapping("/users")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(
                authService.getAllUsers().stream()
                        .map(this::toUserProfileResponse)
                        .collect(Collectors.toList())
        );
    }

    // ── Helpers ───────────────────────────────────────────────────
    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or malformed");
        }
        return authHeader.substring(7);
    }

    private UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .currency(user.getCurrency())
                .timezone(user.getTimezone())
                .avatarUrl(user.getAvatarUrl())
                .monthlyBudget(user.getMonthlyBudget())
                .bio(user.getBio())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .build();
    }
}
