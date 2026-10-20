# AirWatch - Full Stack Java Mini Project Setup Guide

## Overview

This project demonstrates all 28 topics from the Full Stack Java Lab Manual:
- **Backend**: Java Spring Boot (REST API, JPA, MySQL)
- **Frontend**: React (Simple, no AI features)
- **Database**: MySQL

---

## Prerequisites

Before starting, ensure you have:

1. **Java 17 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```

4. **Node.js 18+ and npm**
   ```bash
   node -version
   npm -version
   ```

---

## Part 1: Database Setup

### Step 1: Create MySQL Database

1. Open MySQL command line or MySQL Workbench

2. Create the database:
```sql
CREATE DATABASE airwatch_db;
```

3. Create a user (optional):
```sql
CREATE USER 'airwatch_user'@'localhost' IDENTIFIED BY 'airwatch_pass';
GRANT ALL PRIVILEGES ON airwatch_db.* TO 'airwatch_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 2: Update Database Configuration

Edit `backend-java/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/airwatch_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with your MySQL password.

---

## Part 2: Backend Setup (Java Spring Boot)

### Step 1: Navigate to Backend Directory

```bash
cd backend-java
```

### Step 2: Build the Project

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the Java code
- Run tests (if any)
- Package the application

### Step 3: Run the Backend

```bash
mvn spring-boot:run
```

**Expected Output:**
```
╔══════════════════════════════════════════════╗
║   AirWatch Application Started Successfully  ║
║   Server running on: http://localhost:8080   ║
╚══════════════════════════════════════════════╝
```

### Step 4: Verify Backend is Running

Open browser or use curl:
```bash
curl http://localhost:8080/api/users
```

You should see an empty array `[]` or sample users.

---

## Part 3: Frontend Setup (React)

### Step 1: Navigate to Frontend Directory

Open a **new terminal** and run:

```bash
cd frontend-simple
```

### Step 2: Install Dependencies

```bash
npm install
```

### Step 3: Run the Frontend

```bash
npm run dev
```

**Expected Output:**
```
  VITE v5.0.8  ready in 500 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
```

### Step 4: Open in Browser

Open `http://localhost:5173` in your web browser.

---

## Part 4: Testing the Application

### Test User Registration

1. Click "Register" tab
2. Fill in the form:
   - Username: `testuser`
   - Email: `test@example.com`
   - Password: `test123`
   - Full Name: `Test User`
   - Role: `Citizen`
3. Click "Register"

### Test Login

1. Click "Login" tab
2. Enter:
   - Username: `testuser`
   - Password: `test123`
3. Click "Login"

### Test Submit Report

1. After login, click "Submit Report"
2. Fill in the form:
   - Description: `Heavy pollution from nearby construction`
   - Location Name: `Mumbai`
   - Click "Get Location" (or manually enter coordinates)
   - Latitude: `19.0760`
   - Longitude: `72.8777`
   - Severity: `4`
3. Click "Submit Report"

### Test View Reports

1. Click "My Reports" tab
2. You should see the report you just submitted

---

## Part 5: API Testing (Optional)

### Using curl

**Register User:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "apiuser",
    "email": "api@example.com",
    "password": "api123",
    "fullName": "API User",
    "role": "CITIZEN"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "apiuser",
    "password": "api123"
  }'
```

**Create Report:**
```bash
curl -X POST http://localhost:8080/api/reports \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "description": "Pollution from industrial area causing breathing issues",
    "latitude": 19.0760,
    "longitude": 72.8777,
    "locationName": "Andheri, Mumbai",
    "severity": 4
  }'
```

**Get All Reports:**
```bash
curl http://localhost:8080/api/reports
```

---

## Project Structure

```
gdgc air project/
├── backend-java/                 # Java Spring Boot Backend
│   ├── src/main/java/com/airwatch/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business Logic
│   │   ├── repository/          # Data Access Layer
│   │   ├── model/               # JPA Entities
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Custom Exceptions
│   │   ├── config/              # Configuration Classes
│   │   └── util/                # Utility Classes
│   ├── src/main/resources/
│   │   ├── application.properties  # Configuration
│   │   └── data.sql             # Sample Data
│   └── pom.xml                  # Maven Dependencies
│
└── frontend-simple/             # React Frontend
    ├── src/
    │   ├── App.jsx              # Main Component
    │   ├── App.css              # Styling
    │   ├── main.jsx             # Entry Point
    │   └── index.css            # Global Styles
    ├── index.html               # HTML Template
    ├── package.json             # npm Dependencies
    └── vite.config.js           # Vite Config
```

---

## Troubleshooting

### Backend Issues

**Problem: Port 8080 already in use**
```
Solution: Change port in application.properties:
server.port=8081
```

**Problem: MySQL connection refused**
```
Solution: 
1. Check if MySQL is running: sudo service mysql start
2. Verify credentials in application.properties
3. Check if database exists: SHOW DATABASES;
```

**Problem: JPA table creation fails**
```
Solution:
1. Set spring.jpa.hibernate.ddl-auto=create (first run only)
2. Or manually create tables using data.sql
```

### Frontend Issues

**Problem: Port 5173 already in use**
```
Solution: Change port in vite.config.js:
server: { port: 5174 }
```

**Problem: Cannot connect to backend**
```
Solution:
1. Verify backend is running on port 8080
2. Check API_BASE in App.jsx
3. Check CORS configuration in backend
```

---

## Default Test Users

After running the application, you can use these default users:

| Username  | Password    | Role              |
|-----------|-------------|-------------------|
| admin     | admin123    | ADMIN             |
| officer1  | officer123  | MUNICIPAL_OFFICER |
| citizen1  | citizen123  | CITIZEN           |

---

## Topics Covered Checklist

### Core Java (Topics 1-2, 6, 12, 25)
- [x] Classes, Objects, Methods
- [x] Constructors, Variables
- [x] Encapsulation
- [x] Inheritance
- [x] Polymorphism
- [x] Method/Constructor Overloading
- [x] Exception Handling (try-catch-finally, custom exceptions)
- [x] Collections (List, HashMap, Streams)
- [x] Enums

### Database & JDBC (Topics 3, 22)
- [x] MySQL Connection
- [x] JDBC Operations (via JPA)
- [x] PreparedStatement (via JPA)
- [x] Database Schema Design
- [x] CRUD Operations

### Spring Boot (Topics 9, 18)
- [x] Spring Boot Application
- [x] REST Controllers
- [x] Service Layer
- [x] Repository Layer (DAO)
- [x] JPA/Hibernate
- [x] Dependency Injection

### Multithreading (Topic 7)
- [x] @Async annotations
- [x] Thread Pool Configuration
- [x] Concurrent task execution

### REST API (Topic 19)
- [x] HTTP Methods (GET, POST, PUT, DELETE)
- [x] Status Codes
- [x] JSON Request/Response
- [x] CORS Configuration
- [x] Error Handling

### React & Frontend (Topics 5, 8, 13, 14, 21, 26)
- [x] React Components
- [x] JSX
- [x] Props & State
- [x] Event Handling
- [x] HTML/CSS
- [x] Form Validation
- [x] DOM Manipulation
- [x] fetch() API
- [x] Geolocation API
- [x] LocalStorage

### Authentication & Security (Topics 11, 23)
- [x] Login/Logout
- [x] BCrypt Password Hashing
- [x] Input Validation
- [x] SQL Injection Prevention

---

## Next Steps for Lab Submission

1. **Add JSP Pages** (Topic 4): Convert some React pages to JSP
2. **Add Servlets** (Topic 17): Create servlet examples
3. **Add Tests** (Topic 27): Write JUnit tests
4. **Documentation**: Add JavaDoc comments
5. **ER Diagram**: Draw database relationships
6. **Deployment**: Deploy to cloud platform

---

## Support

If you encounter any issues:

1. Check logs in console
2. Verify database connection
3. Ensure all ports are available
4. Review configuration files

For more details, see:
- `backend-java/README.md`
- `frontend-simple/README.md`
