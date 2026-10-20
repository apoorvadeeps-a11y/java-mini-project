package com.airwatch.exception;

/**
 * Custom Exception for User Already Exists
 * 
 * Topics Covered:
 * - Custom exceptions
 * - Exception inheritance
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
