package com.airwatch.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Email Service (Simplified for Lab Demo)
 * 
 * Topics Covered:
 * - Utility classes
 * - String formatting
 * - Logging
 */
@Service
@Slf4j
public class EmailService {

    /**
     * Send report confirmation email
     * Simplified version - logs instead of sending actual email
     */
    public void sendReportConfirmation(String toEmail, String location, Integer severity) {
        String subject = "AirWatch: Report Received";
        String body = String.format(
                "Thank you for your pollution report!\n\n" +
                "Location: %s\n" +
                "Severity: %d/5\n\n" +
                "We have received your report and will verify it shortly.\n\n" +
                "Best regards,\nAirWatch Team",
                location, severity
        );
        
        log.info("====== EMAIL SENT ======");
        log.info("To: {}", toEmail);
        log.info("Subject: {}", subject);
        log.info("Body:\n{}", body);
        log.info("========================");
    }

    /**
     * Send alert notification
     */
    public void sendAlertNotification(String toEmail, String alertMessage) {
        log.info("====== ALERT SENT ======");
        log.info("To: {}", toEmail);
        log.info("Alert: {}", alertMessage);
        log.info("========================");
    }
}
