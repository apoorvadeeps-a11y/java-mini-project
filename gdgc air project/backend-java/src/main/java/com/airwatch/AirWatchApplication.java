package com.airwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Application Entry Point
 * 
 * Topics Covered:
 * - Core Java: Classes, methods, main method
 * - Spring Boot Fundamentals: @SpringBootApplication annotation
 * - Software Engineering: Application structure
 */
@SpringBootApplication
@EnableAsync
public class AirWatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirWatchApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   AirWatch Application Started Successfully  ║");
        System.out.println("║   Server running on: http://localhost:8080   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
