package com.airwatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async Configuration
 * 
 * Topics Covered:
 * - Multithreading: Thread pool configuration
 * - Concurrency: Async task execution
 * - Spring Boot Configuration
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Configure thread pool for async tasks
     * Topics: Multithreading, thread pool management
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("airwatch-async-");
        executor.initialize();
        return executor;
    }
}
