package com.airwatch.controller;

import com.airwatch.dto.LoginRequest;
import com.airwatch.dto.UserDTO;
import com.airwatch.model.User;
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
 * User REST Controller
 * 
 * Topics Covered:
 * - Spring Boot REST Controller
 * - HTTP Methods: GET, POST, PUT, DELETE
 * - REST API Design: Status codes, JSON responses
 * - Request/Response handling
 * - Servlet Concepts: Request mapping, path variables
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Register a new user (POST)
     * Topics: HTTP POST, JSON request body, validation
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("POST /users/register - Registering user: {}", userDTO.getUsername());
        
        User user = convertToEntity(userDTO);
        User savedUser = userService.registerUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("user", convertToDTO(savedUser));
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Login user (POST)
     * Topics: Authentication, session management basics
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("POST /users/login - User: {}", loginRequest.getUsername());
        
        User user = userService.authenticateUser(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("user", convertToDTO(user));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users (GET)
     * Topics: HTTP GET, JSON array response
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("GET /users - Fetching all users");
        
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Get user by ID (GET with path variable)
     * Topics: Path variables, URL parameters
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("GET /users/{} - Fetching user", id);
        
        User user = userService.getUserById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    /**
     * Get users by role (GET with request param)
     * Topics: Query parameters, enums
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable User.UserRole role) {
        log.info("GET /users/role/{} - Fetching users by role", role);
        
        List<User> users = userService.getUsersByRole(role);
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Update user (PUT)
     * Topics: HTTP PUT, partial updates
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        log.info("PUT /users/{} - Updating user", id);
        
        User userDetails = convertToEntity(userDTO);
        User updatedUser = userService.updateUser(id, userDetails);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User updated successfully");
        response.put("user", convertToDTO(updatedUser));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete user (DELETE)
     * Topics: HTTP DELETE, status codes
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{} - Deleting user", id);
        
        userService.deleteUser(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivate user (POST)
     * Topics: Soft delete pattern
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateUser(@PathVariable Long id) {
        log.info("POST /users/{}/deactivate", id);
        
        userService.deactivateUser(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User deactivated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Helper methods to convert between Entity and DTO
     * Topics: Data Transfer Objects, encapsulation
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        // Never expose password in DTO
        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        return user;
    }
}
