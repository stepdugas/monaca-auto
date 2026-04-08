package com.dealership.api.controller;

import com.dealership.api.dto.LoginRequest;
import com.dealership.api.model.ContactSubmission;
import com.dealership.api.model.DealershipConfig;
import com.dealership.api.repository.ContactSubmissionRepository;
import com.dealership.api.security.JwtTokenProvider;
import com.dealership.api.service.DealershipConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Manager-tier API endpoints.
 *
 * Public:   POST /api/manager/login
 * Manager+: GET  /api/manager/contacts
 *           GET  /api/manager/settings
 *           PUT  /api/manager/settings
 *           PUT  /api/manager/settings/{key}
 *
 * Managers can manage inventory via the existing /api/inventory endpoints
 * (those endpoints now accept MANAGER role).
 *
 * Managers CANNOT access /api/admin/** (user management, system config).
 */
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ContactSubmissionRepository contactRepo;
    private final DealershipConfigService configService;

    @Value("${dealership.manager.username}")
    private String managerUsername;

    @Value("${dealership.manager.password}")
    private String managerPassword;

    public ManagerController(JwtTokenProvider jwtTokenProvider,
                             ContactSubmissionRepository contactRepo,
                             DealershipConfigService configService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.contactRepo = contactRepo;
        this.configService = configService;
    }

    // ── Authentication ────────────────────────────────────────────────

    /**
     * POST /api/manager/login
     * Returns a JWT with MANAGER role on valid credentials.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        String effectivePassword = configService.get("manager_password");
        if (effectivePassword == null) effectivePassword = managerPassword;

        if (!managerUsername.equals(req.getUsername()) || !effectivePassword.equals(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtTokenProvider.generateToken(req.getUsername(), "MANAGER");
        return ResponseEntity.ok(Map.of("token", token, "role", "MANAGER"));
    }

    // ── Contact submissions ───────────────────────────────────────────

    /**
     * GET /api/manager/contacts
     * Returns all contact form submissions, newest first.
     */
    @GetMapping("/contacts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ContactSubmission>> getContacts() {
        return ResponseEntity.ok(contactRepo.findAllByOrderByCreatedAtDesc());
    }

    // ── Store settings (hours, contact info) ──────────────────────────

    /**
     * GET /api/manager/settings
     * Returns all dealership_config entries as a flat key/value map.
     */
    @GetMapping("/settings")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> getSettings() {
        return ResponseEntity.ok(configService.getAll());
    }

    /**
     * PUT /api/manager/settings
     * Bulk-update config entries. Body: { "key": "value", ... }
     */
    @PutMapping("/settings")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody Map<String, String> updates) {
        return ResponseEntity.ok(configService.setAll(updates));
    }

    /**
     * PUT /api/manager/settings/{key}
     * Update a single config entry. Body: { "value": "..." }
     */
    @PutMapping("/settings/{key}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DealershipConfig> updateSetting(
            @PathVariable String key,
            @RequestBody Map<String, String> body) {
        String value = body.get("value");
        return ResponseEntity.ok(configService.set(key, value));
    }
}
