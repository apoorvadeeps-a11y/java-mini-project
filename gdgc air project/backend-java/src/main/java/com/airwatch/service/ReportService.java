package com.airwatch.service;

import com.airwatch.exception.ResourceNotFoundException;
import com.airwatch.model.Report;
import com.airwatch.model.User;
import com.airwatch.repository.ReportRepository;
import com.airwatch.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Report Service Layer
 * 
 * Topics Covered:
 * - Spring Boot Service Layer
 * - Multithreading: @Async annotation for background tasks
 * - Exception Handling
 * - Collections: Working with Lists
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;
    private final EmailService emailService;

    /**
     * Create a new pollution report
     * Topics: Transaction management, exception handling
     */
    @Transactional
    public Report createReport(Report report) {
        try {
            log.info("Creating new report by user: {}", report.getUser().getId());
            
            // Validate user exists
            User user = userService.getUserById(report.getUser().getId());
            report.setUser(user);
            report.setStatus(Report.ReportStatus.PENDING);
            report.setCreatedAt(LocalDateTime.now());
            
            Report savedReport = reportRepository.save(report);
            log.info("Report created successfully with ID: {}", savedReport.getId());
            
            // Send confirmation email asynchronously
            sendReportConfirmationAsync(savedReport);
            
            return savedReport;
            
        } catch (Exception e) {
            log.error("Error creating report: {}", e.getMessage());
            throw new RuntimeException("Failed to create report", e);
        }
    }

    /**
     * Async method to send confirmation email
     * Topics: Multithreading, asynchronous processing
     */
    @Async
    public CompletableFuture<Void> sendReportConfirmationAsync(Report report) {
        try {
            log.info("Sending confirmation email for report ID: {}", report.getId());
            Thread.sleep(1000); // Simulate email sending delay
            
            emailService.sendReportConfirmation(
                    report.getUser().getEmail(),
                    report.getLocationName(),
                    report.getSeverity()
            );
            
            log.info("Confirmation email sent successfully");
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("Failed to send confirmation email: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Get report by ID
     */
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    /**
     * Get all reports
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Get reports by user
     */
    public List<Report> getReportsByUser(Long userId) {
        User user = userService.getUserById(userId);
        return reportRepository.findByUser(user);
    }

    /**
     * Get reports by status
     */
    public List<Report> getReportsByStatus(Report.ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    /**
     * Get nearby reports within radius
     * Topics: Geographic calculations, method parameters
     */
    public List<Report> getNearbyReports(Double latitude, Double longitude, Double radiusKm) {
        try {
            log.info("Finding reports within {}km of ({}, {})", radiusKm, latitude, longitude);
            return reportRepository.findNearbyReports(latitude, longitude, radiusKm);
        } catch (Exception e) {
            log.error("Error finding nearby reports: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch nearby reports", e);
        }
    }

    /**
     * Get severe reports
     */
    public List<Report> getSevereReports() {
        return reportRepository.findSevereReportsByStatus(Report.ReportStatus.VERIFIED);
    }

    /**
     * Verify a report (Municipal Officer action)
     */
    @Transactional
    public Report verifyReport(Long reportId, Long officerId) {
        Report report = getReportById(reportId);
        User officer = userService.getUserById(officerId);
        
        if (officer.getRole() != User.UserRole.MUNICIPAL_OFFICER && 
            officer.getRole() != User.UserRole.ADMIN) {
            throw new RuntimeException("Only municipal officers can verify reports");
        }
        
        report.setStatus(Report.ReportStatus.VERIFIED);
        report.setVerifiedBy(officer);
        report.setVerifiedAt(LocalDateTime.now());
        
        log.info("Report {} verified by officer {}", reportId, officerId);
        return reportRepository.save(report);
    }

    /**
     * Resolve a report
     */
    @Transactional
    public Report resolveReport(Long reportId) {
        Report report = getReportById(reportId);
        report.setStatus(Report.ReportStatus.RESOLVED);
        log.info("Report {} marked as resolved", reportId);
        return reportRepository.save(report);
    }

    /**
     * Delete a report
     */
    @Transactional
    public void deleteReport(Long id) {
        Report report = getReportById(id);
        reportRepository.delete(report);
        log.info("Report {} deleted", id);
    }

    /**
     * Get report count by user
     */
    public long getReportCountByUser(Long userId) {
        User user = userService.getUserById(userId);
        return reportRepository.countByUser(user);
    }
}
