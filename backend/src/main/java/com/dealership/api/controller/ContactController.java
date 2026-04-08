package com.dealership.api.controller;

import com.dealership.api.dto.ContactRequest;
import com.dealership.api.model.ContactSubmission;
import com.dealership.api.repository.CarRepository;
import com.dealership.api.service.ContactService;
import com.dealership.api.service.DealershipConfigService;
import com.dealership.api.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for contact form submissions and client intake.
 *
 * Public:  POST /api/contact                    (vehicle inquiry)
 * Public:  POST /api/client-intake              (client intake form)
 * Public:  GET  /api/public/settings            (site config for frontend)
 * Admin:   GET  /api/admin/contacts             (view submissions)
 */
@RestController
public class ContactController {

    private final ContactService contactService;
    private final EmailService emailService;
    private final DealershipConfigService configService;
    private final CarRepository carRepository;

    public ContactController(ContactService contactService, EmailService emailService,
                             DealershipConfigService configService, CarRepository carRepository) {
        this.contactService = contactService;
        this.emailService = emailService;
        this.configService = configService;
        this.carRepository = carRepository;
    }

    /**
     * GET /api/public/settings — returns all dealership config for the frontend.
     * No authentication required.
     */
    @GetMapping("/api/public/settings")
    public ResponseEntity<Map<String, String>> getPublicSettings() {
        return ResponseEntity.ok(configService.getAll());
    }

    /**
     * POST /api/contact — submit a contact form (public, vehicle inquiry)
     */
    @PostMapping("/api/contact")
    public ResponseEntity<ContactSubmission> submit(@Valid @RequestBody ContactRequest req) {
        ContactSubmission saved = contactService.save(req);
        String vehicleInfo = saved.getCarId() != null ? "Vehicle ID #" + saved.getCarId() : "General Inquiry";
        emailService.sendContactFormEmail(saved.getName(), saved.getEmail(), saved.getPhone(), saved.getMessage(), vehicleInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * POST /api/financing-application — built-in financing application form (public)
     * Emails the dealer's notification_email with the applicant's info.
     */
    @PostMapping("/api/financing-application")
    public ResponseEntity<Map<String, String>> submitFinancingApplication(@RequestBody Map<String, String> body) {
        String notificationEmail = configService.get("notification_email");
        if (notificationEmail == null || notificationEmail.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Dealer notification email is not configured."));
        }
        emailService.sendFinancingApplicationEmail(
            notificationEmail,
            body.get("name"),
            body.get("email"),
            body.get("phone"),
            body.get("employment"),
            body.get("monthlyIncome"),
            body.get("notes")
        );
        return ResponseEntity.ok(Map.of("message", "Application submitted successfully."));
    }

    /**
     * POST /api/schedule-service — service appointment request (public)
     */
    @PostMapping("/api/schedule-service")
    public ResponseEntity<Map<String, String>> scheduleService(@RequestBody Map<String, String> body) {
        String notificationEmail = configService.get("notification_email");
        if (notificationEmail == null || notificationEmail.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Dealer notification email is not configured."));
        }
        emailService.sendServiceAppointmentEmail(notificationEmail, body);
        return ResponseEntity.ok(Map.of("message", "Appointment request submitted successfully."));
    }

    /**
     * GET /api/admin/contacts — list all submissions (admin only)
     */
    @GetMapping("/api/admin/contacts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactSubmission>> getAll() {
        return ResponseEntity.ok(contactService.findAll());
    }

    /**
     * GET /api/admin/dashboard — summary stats for the admin dashboard home page.
     */
    @GetMapping("/api/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("carsAvailable", carRepository.countByStatus("available"));
        stats.put("carsPending",   carRepository.countByStatus("pending"));
        stats.put("carsSold",      carRepository.countByStatus("sold"));
        stats.put("totalContacts", contactService.findAll().size());
        // Most recent 5 contacts
        List<ContactSubmission> recent = contactService.findAll();
        stats.put("recentContacts", recent.subList(0, Math.min(5, recent.size())));
        return ResponseEntity.ok(stats);
    }
}
