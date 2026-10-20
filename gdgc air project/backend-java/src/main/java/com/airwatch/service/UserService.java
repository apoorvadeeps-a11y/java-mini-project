package com.airwatch.service;

import com.airwatch.exception.ResourceNotFoundException;
import com.airwatch.exception.UserAlreadyExistsException;
import com.airwatch.model.User;
import com.airwatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User Service Layer
 * 
 * Topics Covered:
 * - Spring Boot Layered Architecture: Service layer
 * - Exception Handling: Custom exceptions, try-catch
 * - Security: Password hashing with BCrypt
 * - Transaction Management: @Transactional
 * - OOP: Method overloading
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user
     * Topics: Exception handling, password security
     */
    @Transactional
    public User registerUser(User user) {
        log.info("Attempting to register user: {}", user.getUsername());
        
        try {
            // Validate username uniqueness
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
            }
            
            // Validate email uniqueness
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
            }
            
            // Hash password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);
            
            User savedUser = userRepository.save(user);
            log.info("User registered successfully: {}", savedUser.getUsername());
            return savedUser;
            
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during registration: {}", e.getMessage());
            throw new RuntimeException("Failed to register user", e);
        }
    }

    /**
     * Authenticate user - Method Overloading
     */
    public User authenticateUser(String username, String password) {
        return authenticateUser(username, password, true);
    }

    /**
     * Authenticate user with option to update last login
     * Topics: Method overloading, exception handling
     */
    @Transactional
    public User authenticateUser(String username, String password, boolean updateLastLogin) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
            
            if (!user.getActive()) {
                throw new RuntimeException("User account is deactivated");
            }
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            
            if (updateLastLogin) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
            }
            
            log.info("User authenticated: {}", username);
            return user;
            
        } catch (ResourceNotFoundException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        } finally {
            // Finally block example
            log.debug("Authentication attempt completed for: {}", username);
        }
    }

    /**
     * Get user by ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Update user profile
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);
        
        if (userDetails.getFullName() != null) {
            existingUser.setFullName(userDetails.getFullName());
        }
        if (userDetails.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }
            existingUser.setEmail(userDetails.getEmail());
        }
        
        return userRepository.save(existingUser);
    }

    /**
     * Delete user
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
        log.info("User deleted: {}", user.getUsername());
    }

    /**
     * Deactivate user (soft delete)
     */
    @Transactional
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated: {}", user.getUsername());
    }
}
