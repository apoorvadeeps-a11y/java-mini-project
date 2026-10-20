package com.airwatch.repository;

import com.airwatch.model.Report;
import com.airwatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report Repository
 * 
 * Topics Covered:
 * - Database Design: Queries, filtering
 * - Spring Data JPA: Repository pattern
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUser(User user);

    List<Report> findByStatus(Report.ReportStatus status);

    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find reports within radius (simplified Haversine)
     * Topics: Geographic calculations, custom queries
     */
    @Query(value = "SELECT * FROM reports WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(latitude)))) <= :radiusKm " +
            "ORDER BY created_at DESC",
            nativeQuery = true)
    List<Report> findNearbyReports(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radiusKm") Double radiusKm
    );

    /**
     * Find severe reports (severity >= 4)
     */
    @Query("SELECT r FROM Report r WHERE r.severity >= 4 AND r.status = :status ORDER BY r.createdAt DESC")
    List<Report> findSevereReportsByStatus(@Param("status") Report.ReportStatus status);

    /**
     * Count reports by user
     */
    long countByUser(User user);
}
