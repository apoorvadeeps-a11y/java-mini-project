package com.airwatch.controller;

import com.airwatch.dto.ReportDTO;
import com.airwatch.model.Report;
import com.airwatch.model.User;
import com.airwatch.service.ReportService;
import com.airwatch.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Report REST Controller
 * 
 * Topics Covered:
 * - Servlet Concepts: HTTP request/response, URL mappings
 * - REST API Design: RESTful endpoints, HTTP methods, status codes
 * - Collections: List, streams, map operations
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    /**
     * Create a new report (POST)
     * Topics: HTTP POST, JSON request/response
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReport(@Valid @RequestBody ReportDTO reportDTO) {
        log.info("POST /reports - Creating report by user ID: {}", reportDTO.getUserId());
        
        Report report = convertToEntity(reportDTO);
        Report savedReport = reportService.createReport(report);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Report submitted successfully");
        response.put("report", convertToDTO(savedReport));
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all reports (GET)
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        log.info("GET /reports - Fetching all reports");
        
        List<Report> reports = reportService.getAllReports();
        List<ReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Get report by ID (GET)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        log.info("GET /reports/{}", id);
        
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(convertToDTO(report));
    }

    /**
     * Get reports by user ID (GET)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(@PathVariable Long userId) {
        log.info("GET /reports/user/{}", userId);
        
        List<Report> reports = reportService.getReportsByUser(userId);
        List<ReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Get reports by status (GET with request param)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportDTO>> getReportsByStatus(@PathVariable Report.ReportStatus status) {
        log.info("GET /reports/status/{}", status);
        
        List<Report> reports = reportService.getReportsByStatus(status);
        List<ReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Get nearby reports (GET with query parameters)
     * Topics: Query parameters, geographic calculations
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<ReportDTO>> getNearbyReports(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "15.0") Double radiusKm) {
        
        log.info("GET /reports/nearby?lat={}&lng={}&radius={}", latitude, longitude, radiusKm);
        
        List<Report> reports = reportService.getNearbyReports(latitude, longitude, radiusKm);
        List<ReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Get severe reports (GET)
     */
    @GetMapping("/severe")
    public ResponseEntity<List<ReportDTO>> getSevereReports() {
        log.info("GET /reports/severe");
        
        List<Report> reports = reportService.getSevereReports();
        List<ReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Verify a report (POST)
     */
    @PostMapping("/{id}/verify")
    public ResponseEntity<Map<String, Object>> verifyReport(
            @PathVariable Long id,
            @RequestParam Long officerId) {
        
        log.info("POST /reports/{}/verify by officer {}", id, officerId);
        
        Report verifiedReport = reportService.verifyReport(id, officerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Report verified successfully");
        response.put("report", convertToDTO(verifiedReport));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Resolve a report (POST)
     */
    @PostMapping("/{id}/resolve")
    public ResponseEntity<Map<String, Object>> resolveReport(@PathVariable Long id) {
        log.info("POST /reports/{}/resolve", id);
        
        Report resolvedReport = reportService.resolveReport(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Report resolved successfully");
        response.put("report", convertToDTO(resolvedReport));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a report (DELETE)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReport(@PathVariable Long id) {
        log.info("DELETE /reports/{}", id);
        
        reportService.deleteReport(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Report deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get report count for user (GET)
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Object>> getReportCount(@PathVariable Long userId) {
        log.info("GET /reports/user/{}/count", userId);
        
        long count = reportService.getReportCountByUser(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("reportCount", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Helper methods for DTO conversion
     */
    private ReportDTO convertToDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setUserId(report.getUser().getId());
        dto.setUsername(report.getUser().getUsername());
        dto.setDescription(report.getDescription());
        dto.setPhotoUrl(report.getPhotoUrl());
        dto.setLatitude(report.getLatitude());
        dto.setLongitude(report.getLongitude());
        dto.setLocationName(report.getLocationName());
        dto.setSeverity(report.getSeverity());
        dto.setStatus(report.getStatus());
        dto.setCreatedAt(report.getCreatedAt());
        return dto;
    }

    private Report convertToEntity(ReportDTO dto) {
        Report report = new Report();
        User user = new User();
        user.setId(dto.getUserId());
        report.setUser(user);
        report.setDescription(dto.getDescription());
        report.setPhotoUrl(dto.getPhotoUrl());
        report.setLatitude(dto.getLatitude());
        report.setLongitude(dto.getLongitude());
        report.setLocationName(dto.getLocationName());
        report.setSeverity(dto.getSeverity());
        return report;
    }
}
