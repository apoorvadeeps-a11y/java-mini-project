package com.airwatch.dto;

import com.airwatch.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User Data Transfer Object
 * 
 * Topics Covered:
 * - DTO Pattern: Separating API contracts from domain models
 * - Validation: Input validation annotations
 * - Encapsulation: Hiding sensitive data (password not exposed in responses)
 */
@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Only used during registration, never returned in responses
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String fullName;
    
    private String phoneNumber;
    
    private User.UserRole role;
    
    private Boolean active;
    
    private LocalDateTime createdAt;
}
