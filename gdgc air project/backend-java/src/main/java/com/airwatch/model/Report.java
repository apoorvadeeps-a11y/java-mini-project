package com.airwatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Pollution Report Entity
 * 
 * Topics Covered:
 * - Core Java: Classes, objects, variables, arrays
 * - OOP: Encapsulation with getters/setters (via Lombok)
 * - JPA: Entity mapping, relationships
 * - Database Design: Foreign keys, table relationships
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Invalid latitude")
    @Max(value = 90, message = "Invalid latitude")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Invalid longitude")
    @Max(value = 180, message = "Invalid longitude")
    private Double longitude;

    @Column(name = "location_name")
    private String locationName;

    @Min(value = 1, message = "Severity must be between 1 and 5")
    @Max(value = 5, message = "Severity must be between 1 and 5")
    private Integer severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    /**
     * Method Overloading Example
     */
    public String getDisplayLocation() {
        return getDisplayLocation(false);
    }

    /**
     * Method Overloading with parameter
     */
    public String getDisplayLocation(boolean includeCoordinates) {
        if (includeCoordinates) {
            return String.format("%s (%.4f, %.4f)", 
                locationName != null ? locationName : "Unknown", 
                latitude, longitude);
        }
        return locationName != null ? locationName : "Unknown Location";
    }

    public enum ReportStatus {
        PENDING,
        VERIFIED,
        RESOLVED,
        REJECTED
    }
}
