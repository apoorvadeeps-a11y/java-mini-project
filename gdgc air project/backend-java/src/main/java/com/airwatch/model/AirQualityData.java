package com.airwatch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Air Quality Data Entity
 * 
 * Topics Covered:
 * - Core Java: Primitive data types, wrapper classes
 * - Database Design: Time series data storage
 * - Advanced Java: Working with LocalDateTime
 */
@Entity
@Table(name = "air_quality_data", indexes = {
    @Index(name = "idx_location", columnList = "latitude,longitude"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirQualityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    
    private Double longitude;

    @Column(name = "location_name")
    private String locationName;

    @Column(nullable = false)
    private Integer aqi;

    private Double pm25;
    
    private Double pm10;
    
    private Double no2;
    
    private Double so2;
    
    private Double co;
    
    private Double ozone;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AQICategory category;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (category == null && aqi != null) {
            category = calculateCategory(aqi);
        }
    }

    /**
     * Calculate AQI Category based on value
     * Topics: Control flow, conditional statements
     */
    public static AQICategory calculateCategory(int aqi) {
        if (aqi <= 50) return AQICategory.GOOD;
        if (aqi <= 100) return AQICategory.MODERATE;
        if (aqi <= 150) return AQICategory.UNHEALTHY_SENSITIVE;
        if (aqi <= 200) return AQICategory.UNHEALTHY;
        if (aqi <= 300) return AQICategory.VERY_UNHEALTHY;
        return AQICategory.HAZARDOUS;
    }

    public enum AQICategory {
        GOOD,
        MODERATE,
        UNHEALTHY_SENSITIVE,
        UNHEALTHY,
        VERY_UNHEALTHY,
        HAZARDOUS
    }
}
