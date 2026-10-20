-- Sample data for AirWatch Database
-- Topics Covered: SQL, Database Design, CRUD operations

-- Insert sample users
INSERT INTO users (username, email, password, full_name, phone_number, role, created_at, active)
VALUES
    ('admin', 'admin@airwatch.com', '$2a$10$rBV2K1p.dQk6Y1r7Z5GY0uF/0mLQZ7x3Z.Hx9K3.3.Lx9K3.3.Lx9K', 'Admin User', '9876543210', 'ADMIN', NOW(), TRUE),
    ('officer1', 'officer1@airwatch.com', '$2a$10$rBV2K1p.dQk6Y1r7Z5GY0uF/0mLQZ7x3Z.Hx9K3.3.Lx9K3.3.Lx9K', 'Municipal Officer', '9876543211', 'MUNICIPAL_OFFICER', NOW(), TRUE),
    ('citizen1', 'citizen1@gmail.com', '$2a$10$rBV2K1p.dQk6Y1r7Z5GY0uF/0mLQZ7x3Z.Hx9K3.3.Lx9K3.3.Lx9K', 'John Doe', '9876543212', 'CITIZEN', NOW(), TRUE),
    ('citizen2', 'citizen2@gmail.com', '$2a$10$rBV2K1p.dQk6Y1r7Z5GY0uF/0mLQZ7x3Z.Hx9K3.3.Lx9K3.3.Lx9K', 'Jane Smith', '9876543213', 'CITIZEN', NOW(), TRUE);

-- Insert sample air quality data
INSERT INTO air_quality_data (latitude, longitude, location_name, aqi, pm25, pm10, no2, so2, timestamp, category)
VALUES
    (19.0760, 72.8777, 'Mumbai Central', 85, 35.0, 65.0, 40.0, 20.0, NOW(), 'MODERATE'),
    (28.6139, 77.2090, 'Delhi Center', 165, 85.0, 150.0, 55.0, 25.0, NOW(), 'UNHEALTHY'),
    (12.9716, 77.5946, 'Bangalore MG Road', 65, 28.0, 50.0, 30.0, 15.0, NOW(), 'MODERATE'),
    (13.0827, 80.2707, 'Chennai Marina', 75, 32.0, 58.0, 35.0, 18.0, NOW(), 'MODERATE');

-- Insert sample reports
INSERT INTO reports (user_id, description, latitude, longitude, location_name, severity, status, created_at)
VALUES
    (3, 'Heavy smoke from nearby construction site affecting air quality', 19.0760, 72.8777, 'Andheri West, Mumbai', 3, 'PENDING', NOW()),
    (4, 'Industrial pollution near residential area with strong chemical smell', 28.6139, 77.2090, 'Sector 62, Noida', 4, 'VERIFIED', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (3, 'Garbage burning causing air pollution in the locality', 12.9716, 77.5946, 'Koramangala, Bangalore', 3, 'RESOLVED', DATE_SUB(NOW(), INTERVAL 2 DAY));
