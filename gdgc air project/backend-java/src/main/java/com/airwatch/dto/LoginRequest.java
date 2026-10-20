package com.airwatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login Request DTO
 * 
 * Topics Covered:
 * - DTOs for specific operations
 * - Input validation
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
