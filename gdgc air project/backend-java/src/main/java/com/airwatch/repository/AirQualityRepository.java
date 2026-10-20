package com.airwatch.repository;

import com.airwatch.model.AirQualityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Air Quality Data Repository
 */
@Repository
public interface AirQualityRepository extends JpaRepository<AirQualityData, Long> {

    List<AirQualityData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * FROM air_quality_data WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(latitude)))) <= :radiusKm " +
            "ORDER BY timestamp DESC LIMIT 1",
            nativeQuery = true)
    AirQualityData findNearestAirQuality(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radiusKm") Double radiusKm
    );
}
