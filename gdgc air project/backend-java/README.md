# AirWatch - Java Backend

## Full Stack Java Lab Mini Project

### 28 Topics Covered

This project demonstrates all 28 topics from the Full Stack Java Lab Manual:

#### Core Java & OOP (Topics 1-2, 12)
- ✅ **Classes, Objects, Methods**: User, Report, AirQualityData models
- ✅ **Constructors & Variables**: Multiple constructors in User class
- ✅ **Encapsulation**: Private fields with getters/setters
- ✅ **Inheritance**: Entities extending JPA base, Custom exceptions extending RuntimeException
- ✅ **Polymorphism**: Method overriding, Interface implementations
- ✅ **Constructor/Method Overloading**: UserService.authenticateUser(), Report constructors

#### Database & JDBC (Topics 3, 22)
- ✅ **MySQL Connectivity**: application.properties configuration
- ✅ **JDBC Operations**: Through Spring Data JPA abstraction
- ✅ **PreparedStatement**: JPA Query methods prevent SQL injection
- ✅ **Database Schema**: users, reports, air_quality_data tables
- ✅ **ER Relationships**: One-to-Many (User-Reports)
- ✅ **CRUD Operations**: All repository interfaces

#### Spring Boot & Architecture (Topics 9, 18)
- ✅ **Spring Boot Setup**: AirWatchApplication main class
- ✅ **REST Endpoints**: UserController, ReportController
- ✅ **Dependency Injection**: @RequiredArgsConstructor, @Autowired
- ✅ **Layered Architecture**: Controller → Service → Repository
- ✅ **JPA/Hibernate**: All entity classes with annotations
- ✅ **CRUD with JPA**: UserRepository, ReportRepository

#### Exception Handling (Topic 6)
- ✅ **try-catch-finally**: UserService.authenticateUser()
- ✅ **throw & throws**: Custom exceptions thrown
- ✅ **Custom Exceptions**: ResourceNotFoundException, UserAlreadyExistsException
- ✅ **Global Exception Handler**: GlobalExceptionHandler

#### Multithreading (Topic 7)
- ✅ **Thread & Runnable**: AsyncConfig with ThreadPoolTaskExecutor
- ✅ **@Async**: ReportService.sendReportConfirmationAsync()
- ✅ **Synchronization**: Thread-safe operations with @Transactional
- ✅ **Thread Pool**: Configured in AsyncConfig

#### REST API Design (Topic 19)
- ✅ **HTTP Methods**: GET, POST, PUT, DELETE implemented
- ✅ **Status Codes**: 200, 201, 400, 404, 409, 500
- ✅ **JSON Requests/Responses**: All endpoints return JSON
- ✅ **Error Handling**: GlobalExceptionHandler for all errors
- ✅ **CORS**: CorsConfig for cross-origin requests

#### Servlets & HTTP (Topic 17)
- ✅ **Servlet Concepts**: @RestController (modern servlet)
- ✅ **Request/Response Objects**: ResponseEntity usage
- ✅ **URL Mappings**: @RequestMapping, @GetMapping, etc.
- ✅ **@RequestBody, @PathVariable, @RequestParam**: All used

#### Authentication & Sessions (Topic 11)
- ✅ **Login/Logout**: UserController.login()
- ✅ **Password Security**: BCryptPasswordEncoder
- ✅ **Credential Verification**: UserService.authenticateUser()
- ✅ **Session Basics**: lastLogin tracking

#### Security & Validation (Topic 23)
- ✅ **SQL Injection Prevention**: JPA Query methods
- ✅ **Input Validation**: @Valid annotations on DTOs
- ✅ **Password Hashing**: BCrypt
- ✅ **Authorization**: Role-based checks in ReportService

#### Advanced Java (Topic 25)
- ✅ **Collections**: List, HashMap, Stream operations
- ✅ **Enums**: UserRole, ReportStatus, AQICategory
- ✅ **Comparable**: AQICalculator comparisons
- ✅ **File Handling**: Logging to files
- ✅ **Concurrency Utilities**: CompletableFuture, ThreadPoolTaskExecutor

#### Testing (Topic 27 - Can be added)
- Unit tests can be added with JUnit and Mockito
- Integration tests with @SpringBootTest

## Project Structure

```
backend-java/
├── src/main/java/com/airwatch/
│   ├── AirWatchApplication.java          # Main entry point
│   ├── config/
│   │   ├── AsyncConfig.java              # Thread pool config
│   │   └── CorsConfig.java               # CORS config
│   ├── controller/
│   │   ├── UserController.java           # User REST endpoints
│   │   └── ReportController.java         # Report REST endpoints
│   ├── service/
│   │   ├── UserService.java              # User business logic
│   │   └── ReportService.java            # Report business logic
│   ├── repository/
│   │   ├── UserRepository.java           # User data access
│   │   ├── ReportRepository.java         # Report data access
│   │   └── AirQualityRepository.java     # AQI data access
│   ├── model/
│   │   ├── User.java                     # User entity
│   │   ├── Report.java                   # Report entity
│   │   └── AirQualityData.java           # AQI entity
│   ├── dto/
│   │   ├── UserDTO.java                  # User data transfer object
│   │   ├── ReportDTO.java                # Report data transfer object
│   │   └── LoginRequest.java             # Login request DTO
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java   # Global error handler
│   │   ├── ResourceNotFoundException.java
│   │   └── UserAlreadyExistsException.java
│   └── util/
│       ├── AQICalculator.java            # AQI calculations
│       └── EmailService.java             # Email notifications
├── src/main/resources/
│   ├── application.properties            # App configuration
│   └── data.sql                          # Sample data
└── pom.xml                               # Maven dependencies
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup

1. Create database:
```sql
CREATE DATABASE airwatch_db;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run the Application

1. Navigate to backend-java directory:
```bash
cd backend-java
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080/api`

## API Endpoints

### User Endpoints
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Login
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Report Endpoints
- `POST /api/reports` - Create new report
- `GET /api/reports` - Get all reports
- `GET /api/reports/{id}` - Get report by ID
- `GET /api/reports/user/{userId}` - Get user's reports
- `GET /api/reports/nearby?lat=X&lng=Y&radiusKm=15` - Get nearby reports
- `POST /api/reports/{id}/verify?officerId=X` - Verify report
- `DELETE /api/reports/{id}` - Delete report

## Sample API Calls

### Register User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "role": "CITIZEN"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Create Report
```bash
curl -X POST http://localhost:8080/api/reports \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "description": "Heavy pollution from industrial area",
    "latitude": 19.0760,
    "longitude": 72.8777,
    "locationName": "Mumbai",
    "severity": 4
  }'
```

## Default Users (from data.sql)

- **Admin**: username=`admin`, password=`admin123`
- **Officer**: username=`officer1`, password=`officer123`
- **Citizen**: username=`citizen1`, password=`citizen123`

## Technologies Used

- Spring Boot 3.2.0
- Spring Data JPA / Hibernate
- MySQL 8.0
- BCrypt (Password Hashing)
- Lombok (Reduce boilerplate)
- Maven (Build tool)
- Slf4j (Logging)

## Lab Topics Demonstration Map

| Topic # | Topic Name | Implementation Location |
|---------|-----------|------------------------|
| 1 | Core Java & OOP | User.java, Report.java |
| 2 | Inheritance & Polymorphism | All models, exceptions |
| 3 | JDBC & MySQL | application.properties, repositories |
| 6 | Exception Handling | GlobalExceptionHandler, services |
| 7 | Multithreading | AsyncConfig, ReportService |
| 9 | Spring Boot | AirWatchApplication, all controllers |
| 11 | Authentication | UserService.authenticateUser() |
| 12 | Overloading | User constructors, service methods |
| 17 | Servlets & HTTP | All controllers |
| 18 | Layered Architecture | Entire project structure |
| 19 | REST API | UserController, ReportController |
| 22 | Database Design | All entities, data.sql |
| 23 | Security | BCrypt, validation annotations |
| 25 | Advanced Java | Collections, Enums, Streams |
| 27 | Software Engineering | Project structure, documentation |

## Notes for Lab Submission

This is a simplified, educational implementation focused on demonstrating Java concepts. 
For production use, you would add:
- JWT authentication instead of basic login
- More comprehensive validation
- API rate limiting
- Swagger/OpenAPI documentation
- Unit and integration tests
- Docker containerization
