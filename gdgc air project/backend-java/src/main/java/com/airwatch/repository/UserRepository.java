package com.airwatch.repository;

import com.airwatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface
 * 
 * Topics Covered:
 * - Spring Boot: Repository layer, Data Access Object (DAO) pattern
 * - OOP: Interfaces
 * - Database: CRUD operations, custom queries
 * - JDBC: Abstracted through Spring Data JPA
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     * Spring Data JPA query derivation
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     */
    List<User> findByActiveTrue();

    /**
     * Find users by role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Custom JPQL query example
     * Topics: Advanced queries, JPQL
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = true")
    List<User> findActiveUsersByRole(@Param("role") User.UserRole role);

    /**
     * Native SQL query example
     */
    @Query(value = "SELECT * FROM users WHERE full_name LIKE %:keyword%", nativeQuery = true)
    List<User> searchByFullName(@Param("keyword") String keyword);
}
