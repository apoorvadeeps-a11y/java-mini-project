package com.airwatch.dto;

import com.airwatch.model.Report;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Report Data Transfer Object
 * 
 * Topics Covered:
 * - DTO pattern
 * - Validation annotations
 */
@Data
public class ReportDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    private String photoUrl;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Invalid latitude")
    @Max(value = 90, message = "Invalid latitude")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Invalid longitude")
    @Max(value = 180, message = "Invalid longitude")
    private Double longitude;

    private String locationName;

    @NotNull(message = "Severity is required")
    @Min(value = 1, message = "Severity must be between 1 and 5")
    @Max(value = 5, message = "Severity must be between 1 and 5")
    private Integer severity;

    private Report.ReportStatus status;
    
    private LocalDateTime createdAt;
    
    private String username; // For display purposes
}
