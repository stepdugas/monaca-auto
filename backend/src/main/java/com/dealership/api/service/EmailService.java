package com.dealership.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Email service for sending transactional emails.
 * Currently supports Gmail SMTP.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${dealership.email.from}")
    private String fromEmail;

    @Value("${dealership.email.admin-to}")
    private String adminEmail;

    @Value("${dealership.email.reply-to}")
    private String replyToEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send a simple contact form email (for vehicle inquiries).
     */
    public void sendContactFormEmail(String name, String email, String phone, String message, String vehicleInfo) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(adminEmail);
            mailMessage.setReplyTo(email);
            mailMessage.setSubject("📧 New Vehicle Inquiry: " + vehicleInfo);

            String body = String.format(
                    "Name: %s\nEmail: %s\nPhone: %s\n\nVehicle: %s\n\nMessage:\n%s",
                    name, email, phone, vehicleInfo, message
            );
            mailMessage.setText(body);

            mailSender.send(mailMessage);
            logger.info("Contact form email sent for inquiry from {}", email);
        } catch (Exception e) {
            logger.error("Failed to send contact form email", e);
            // Don't throw — log and continue
        }
    }

    /**
     * Send a financing application email to the dealer.
     */
    public void sendFinancingApplicationEmail(String toEmail, String name, String email,
                                              String phone, String employment,
                                              String monthlyIncome, String notes) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setReplyTo(email);
            message.setSubject("💰 New Financing Application: " + name);

            String body = String.format(
                "FINANCING APPLICATION\n\n" +
                "Name:              %s\n" +
                "Email:             %s\n" +
                "Phone:             %s\n" +
                "Employment Status: %s\n" +
                "Monthly Income:    %s\n\n" +
                "Notes:\n%s",
                name, email, phone != null ? phone : "(not provided)",
                employment != null ? employment : "(not provided)",
                monthlyIncome != null ? monthlyIncome : "(not provided)",
                notes != null && !notes.isBlank() ? notes : "(none)"
            );
            message.setText(body);

            mailSender.send(message);
            logger.info("Financing application email sent for {}", email);
        } catch (Exception e) {
            logger.error("Failed to send financing application email", e);
        }
    }

    /**
     * Send a service appointment request email to the dealer.
     */
    public void sendServiceAppointmentEmail(String toEmail, Map<String, String> fields) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            String replyTo = fields.get("email");
            if (replyTo != null && !replyTo.isBlank()) message.setReplyTo(replyTo);
            message.setSubject("🔧 New Service Appointment Request: " + fields.getOrDefault("name", "Unknown"));

            StringBuilder body = new StringBuilder("SERVICE APPOINTMENT REQUEST\n\n");
            body.append("CUSTOMER\n--------\n");
            body.append("Name:    ").append(fields.getOrDefault("name", "")).append("\n");
            body.append("Email:   ").append(fields.getOrDefault("email", "")).append("\n");
            body.append("Phone:   ").append(fields.getOrDefault("phone", "(not provided)")).append("\n\n");
            body.append("SERVICE\n-------\n");
            body.append("Type:    ").append(fields.getOrDefault("serviceType", "")).append("\n");
            if (fields.containsKey("description") && !fields.get("description").isBlank())
                body.append("Notes:   ").append(fields.get("description")).append("\n");
            body.append("\n");
            if (fields.containsKey("vehicleYear") || fields.containsKey("vehicleMake")) {
                body.append("VEHICLE\n-------\n");
                body.append("Year:    ").append(fields.getOrDefault("vehicleYear", "")).append("\n");
                body.append("Make:    ").append(fields.getOrDefault("vehicleMake", "")).append("\n");
                body.append("Model:   ").append(fields.getOrDefault("vehicleModel", "")).append("\n");
                if (fields.containsKey("vehicleMileage") && !fields.get("vehicleMileage").isBlank())
                    body.append("Mileage: ").append(fields.get("vehicleMileage")).append("\n");
                if (fields.containsKey("vehicleVin") && !fields.get("vehicleVin").isBlank())
                    body.append("VIN:     ").append(fields.get("vehicleVin")).append("\n");
                body.append("\n");
            }
            if (fields.containsKey("preferredDate") || fields.containsKey("preferredTime")) {
                body.append("APPOINTMENT PREFERENCE\n----------------------\n");
                if (fields.containsKey("preferredDate") && !fields.get("preferredDate").isBlank())
                    body.append("Date:    ").append(fields.get("preferredDate")).append("\n");
                if (fields.containsKey("preferredTime") && !fields.get("preferredTime").isBlank())
                    body.append("Time:    ").append(fields.get("preferredTime")).append("\n");
                body.append("\n");
            }
            if (fields.containsKey("referralSource") && !fields.get("referralSource").isBlank())
                body.append("How they heard about us: ").append(fields.get("referralSource")).append("\n");

            message.setText(body.toString());
            mailSender.send(message);
            logger.info("Service appointment email sent for {}", fields.get("email"));
        } catch (Exception e) {
            logger.error("Failed to send service appointment email", e);
        }
    }

    /**
     * Send a test email (for OpenClaw configuration testing).
     * Simple email with custom to/subject/body.
     */
    public void sendTestEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setReplyTo(replyToEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            logger.info("Test email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send test email to {}", to, e);
            throw new RuntimeException("Failed to send test email: " + e.getMessage(), e);
        }
    }
}
