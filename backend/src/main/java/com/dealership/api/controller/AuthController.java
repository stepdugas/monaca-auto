package com.dealership.api.controller;

import com.dealership.api.dto.LoginRequest;
import com.dealership.api.security.JwtTokenProvider;
import com.dealership.api.service.DealershipConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * POST /api/admin/login        — returns a JWT on valid credentials.
 * POST /api/admin/change-password         — admin changes their own password.
 * POST /api/admin/change-manager-password — admin resets the manager password.
 *
 * Passwords are checked from dealership_config table first (set via admin UI),
 * falling back to environment variables on first deploy.
 */
@RestController
@RequestMapping("/api/admin")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final DealershipConfigService configService;

    @Value("${dealership.admin.username}")
    private String adminUsername;

    @Value("${dealership.admin.password}")
    private String adminPasswordFallback;

    public AuthController(JwtTokenProvider jwtTokenProvider, DealershipConfigService configService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.configService = configService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        String effectivePassword = configService.get("admin_password");
        if (effectivePassword == null) effectivePassword = adminPasswordFallback;

        if (!adminUsername.equals(req.getUsername()) || !effectivePassword.equals(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtTokenProvider.generateToken(req.getUsername(), "ADMIN");
        return ResponseEntity.ok(Map.of("token", token, "role", "ADMIN"));
    }

    /** Admin changes their own password. Requires current password to verify. */
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (newPassword == null || newPassword.trim().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "New password must be at least 6 characters"));
        }

        String effectivePassword = configService.get("admin_password");
        if (effectivePassword == null) effectivePassword = adminPasswordFallback;

        if (!effectivePassword.equals(currentPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Current password is incorrect"));
        }

        configService.set("admin_password", newPassword);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    /** Admin resets the manager password — no current password needed. */
    @PostMapping("/change-manager-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeManagerPassword(@RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");

        if (newPassword == null || newPassword.trim().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
        }

        configService.set("manager_password", newPassword);
        return ResponseEntity.ok(Map.of("message", "Manager password updated successfully"));
    }
}
