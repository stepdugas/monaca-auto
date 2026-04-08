package com.dealership.api.controller;

import com.dealership.api.service.EmailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Email endpoints — for testing and sending emails.
 *
 * Public (for OpenClaw):  POST /api/email/test
 * Admin:                  POST /api/email/send (reserved for future use)
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * POST /api/email/test
     * Send a test email. Useful for validating email configuration and OpenClaw integration.
     *
     * Request body:
     * {
     *   "to": "test@example.com",
     *   "subject": "Test Email",
     *   "body": "This is a test message."
     * }
     *
     * Response:
     * {
     *   "status": "success",
     *   "message": "Test email sent to test@example.com"
     * }
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> sendTestEmail(
            @RequestBody TestEmailRequest req) {
        try {
            emailService.sendTestEmail(req.getTo(), req.getSubject(), req.getBody());

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Test email sent to " + req.getTo());

            logger.info("Test email sent successfully to: {}", req.getTo());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to send test email", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to send test email: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Request body for test email endpoint.
     */
    public static class TestEmailRequest {
        @NotBlank(message = "Recipient email is required")
        @Email(message = "Invalid email format")
        private String to;

        @NotBlank(message = "Subject is required")
        private String subject;

        @NotBlank(message = "Body is required")
        private String body;

        // Getters and setters
        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
