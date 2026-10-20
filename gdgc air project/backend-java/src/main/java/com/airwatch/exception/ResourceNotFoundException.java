package com.airwatch.exception;

/**
 * Custom Exception for Resource Not Found
 * 
 * Topics Covered:
 * - Java Exception Handling: Custom exceptions
 * - OOP: Inheritance (extends RuntimeException)
 * - Constructor Overloading
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with message only
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor overloading with message and cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor overloading with cause only
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
