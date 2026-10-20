# 🌿 AirWatch - Full Stack Java Mini Project

A complete air quality monitoring system demonstrating all 28 topics from the Full Stack Java Lab Manual.

## 📚 Technologies Used

### Backend
- **Java 17** with Spring Boot 3.2.0
- **Spring Data JPA** / Hibernate
- **MySQL 8.0** Database
- **Maven** Build Tool
- **BCrypt** Password Security
- **REST APIs** with JSON

### Frontend
- **React 18** with Hooks
- **Vite** Build Tool
- **Pure CSS** (No frameworks)
- **Fetch API** for HTTP calls
- **Geolocation API**

## ✅ 28 Topics Covered

| # | Topic | Implementation |
|---|-------|----------------|
| 1 | Core Java & OOP | User.java, Report.java models |
| 2 | Inheritance & Polymorphism | Entity inheritance, interfaces |
| 3 | JDBC & MySQL | Spring Data JPA repositories |
| 6 | Exception Handling | GlobalExceptionHandler, custom exceptions |
| 7 | Multithreading | @Async, ThreadPoolTaskExecutor |
| 8 | React Fundamentals | useState, useEffect, components |
| 9 | Spring Boot | Controllers, Services, Repositories |
| 11 | Authentication | Login, BCrypt passwords |
| 12 | Method Overloading | Multiple constructors, overloaded methods |
| 13 | JavaScript Validation | Form validation, regex patterns |
| 14 | DOM Manipulation | React state updates, event handling |
| 17 | Servlets & HTTP | @RestController, HTTP methods |
| 18 | Layered Architecture | Controller → Service → Repository |
| 19 | REST API Design | GET, POST, PUT, DELETE endpoints |
| 21 | React & Backend Integration | fetch() API calls, JSON |
| 22 | Database Design | Entity relationships, MySQL schema |
| 23 | Security | BCrypt, input validation, SQL injection prevention |
| 25 | Advanced Java | Collections, Streams, Enums |
| 26 | Browser APIs | Geolocation, LocalStorage |
| 27 | Software Engineering | Project structure, documentation |

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+

### 1. Database Setup
```sql
CREATE DATABASE airwatch_db;
```

### 2. Configure Backend
Edit `backend-java/src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run Backend
```bash
cd backend-java
mvn spring-boot:run
```
Backend runs at: `http://localhost:8080/api`

### 4. Run Frontend
```bash
cd frontend-simple
npm install
npm run dev
```
Frontend runs at: `http://localhost:5173`

## 📁 Project Structure

```
gdgc-air-project/
├── backend-java/              # Spring Boot Backend
│   ├── src/main/java/com/airwatch/
│   │   ├── controller/       # REST Controllers
│   │   ├── service/          # Business Logic
│   │   ├── repository/       # Data Access
│   │   ├── model/            # JPA Entities
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Exception Handling
│   │   ├── config/           # Configuration
│   │   └── util/             # Utilities
│   └── pom.xml               # Maven Dependencies
│
├── frontend-simple/           # React Frontend
│   ├── src/
│   │   ├── App.jsx           # Main Component
│   │   ├── App.css           # Styling
│   │   └── main.jsx          # Entry Point
│   └── package.json          # npm Dependencies
│
├── SETUP_GUIDE.md            # Detailed Setup Instructions
└── README.md                 # This File
```

## 🎯 Features

### User Management
- ✅ User Registration
- ✅ Login/Logout with BCrypt
- ✅ Role-based access (Citizen, Officer, Admin)
- ✅ Session persistence with LocalStorage

### Pollution Reporting
- ✅ Submit pollution reports with location
- ✅ Geolocation API integration
- ✅ Severity rating (1-5)
- ✅ View personal reports
- ✅ Status tracking (Pending, Verified, Resolved)

### Air Quality Data
- ✅ AQI calculation utilities
- ✅ Location-based queries
- ✅ Geographic distance calculations

## 🔌 API Endpoints

### Users
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Authenticate user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Reports
- `POST /api/reports` - Create report
- `GET /api/reports` - Get all reports
- `GET /api/reports/{id}` - Get report by ID
- `GET /api/reports/user/{userId}` - Get user's reports
- `GET /api/reports/nearby?lat=X&lng=Y&radiusKm=15` - Nearby reports
- `POST /api/reports/{id}/verify?officerId=X` - Verify report
- `DELETE /api/reports/{id}` - Delete report

## 🧪 Testing

### Default Test Users
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| officer1 | officer123 | MUNICIPAL_OFFICER |
| citizen1 | citizen123 | CITIZEN |

### Sample API Call
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username": "citizen1", "password": "citizen123"}'
```

## 📖 Documentation

- [Detailed Setup Guide](SETUP_GUIDE.md)
- [Backend README](backend-java/README.md)
- [Frontend README](frontend-simple/README.md)

## 🎓 Academic Purpose

This project is designed for educational purposes to demonstrate:
- Full-stack development with Java & React
- RESTful API design
- Database design and ORM
- Authentication and security
- Modern web development practices

## 📝 License

MIT License - Feel free to use for educational purposes

## 👥 Contributors

Created as a Full Stack Java Lab Mini Project

---

**Note:** This is a simplified educational implementation. For production use, consider adding:
- JWT authentication
- API rate limiting
- Comprehensive testing
- Docker containerization
- CI/CD pipeline
