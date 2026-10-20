# AirWatch - Air Quality Monitoring

A mini project where a user can register, log in, and report air pollution
in their area. The reports are saved in MySQL and listed back to the user.

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Data JPA (Hibernate), MySQL, Maven
- **Frontend:** React (Vite), plain CSS, fetch API

## Features

- Register a new user (Citizen or Municipal Officer)
- Login / logout (password is stored as a BCrypt hash)
- Submit a pollution report (description, location name, latitude, longitude, severity 1-5)
- "Get Location" button that reads the browser's current coordinates
- View your own reports with their status (PENDING / VERIFIED / RESOLVED)

## Project Structure

```
gdgc air project/
├── backend-java/                  # Spring Boot backend
│   └── src/main/java/com/airwatch/
│       ├── controller/            # REST controllers
│       ├── service/               # Business logic
│       ├── repository/            # Database access
│       ├── model/                 # JPA entities (User, Report, AirQualityData)
│       ├── dto/                   # Request/response objects
│       ├── exception/             # Custom exceptions + global handler
│       ├── config/                # CORS and async configuration
│       └── util/                  # AQI calculator, email helper
│
├── frontend/                      # React frontend
│   └── src/
│       ├── App.jsx                # Main component (all screens)
│       ├── App.css                # Styling
│       ├── main.jsx               # React entry point
│       └── index.css              # Global styles
│
├── SETUP_GUIDE.md                 # Step-by-step setup
└── README.md                      # This file
```

## How to Run

See [SETUP_GUIDE.md](SETUP_GUIDE.md) for the full steps. Short version:

```
# 1. Create the database
CREATE DATABASE airwatch_db;

# 2. Backend
cd backend-java
mvn spring-boot:run          # runs at http://localhost:8080/api

# 3. Frontend (new terminal)
cd frontend
npm install
npm run dev                  # runs at http://localhost:5173
```

## API Endpoints

**Users**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Login |
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get one user |
| GET | `/api/users/role/{role}` | List users by role |
| PUT | `/api/users/{id}` | Update a user |
| DELETE | `/api/users/{id}` | Delete a user |
| POST | `/api/users/{id}/deactivate` | Deactivate a user |

**Reports**

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/reports` | Create a report |
| GET | `/api/reports` | List all reports |
| GET | `/api/reports/{id}` | Get one report |
| GET | `/api/reports/user/{userId}` | List one user's reports |
| GET | `/api/reports/user/{userId}/count` | Count a user's reports |
| GET | `/api/reports/status/{status}` | List reports by status |
| GET | `/api/reports/nearby?latitude=&longitude=&radiusKm=` | Reports near a point |
| GET | `/api/reports/severe` | List high-severity reports |
| POST | `/api/reports/{id}/verify?officerId=` | Mark a report verified |
| POST | `/api/reports/{id}/resolve` | Mark a report resolved |
| DELETE | `/api/reports/{id}` | Delete a report |

## Topics We Have Implemented

| # | Topic | Where |
|---|-------|-------|
| 1 | Core Java & OOP (classes, objects, constructors, encapsulation) | `model/User.java`, `model/Report.java` |
| 5 | HTML, CSS, JavaScript | `frontend/index.html`, `frontend/src/App.css` |
| 6 | Exception handling (try/catch/finally, custom exceptions) | `exception/`, `service/` |
| 8 | React fundamentals (components, JSX, useState, useEffect) | `frontend/src/App.jsx` |
| 9 | Spring Boot fundamentals | `AirWatchApplication.java`, `controller/` |
| 11 | Login / authentication | `UserController.login`, `UserService` |
| 12 | Method & constructor overloading | `UserService.authenticateUser`, `Report.getDisplayLocation` |
| 15 | CSS & web design (flexbox, grid, media query) | `frontend/src/App.css` |
| 18 | Layered architecture (controller -> service -> repository -> entity) | `backend-java` packages |
| 19 | REST API design (methods, status codes, JSON, CORS) | `controller/`, `config/CorsConfig.java` |
| 21 | React + backend integration (fetch, JSON) | `frontend/src/App.jsx` |
| 22 | Database & application design (entities, relations, schema) | `model/`, `resources/data.sql` |
| 23 | Security (partial) - BCrypt hashing, input validation, error handling | `UserService`, `GlobalExceptionHandler` |
| 25 | Advanced Java (partial) - List, HashMap, streams, enums | `service/`, `controller/` |
| 26 | Browser APIs (partial) - geolocation, localStorage | `frontend/src/App.jsx` |

## Topics Not Implemented Yet

| # | Topic | Note |
|---|-------|------|
| 2 | Inheritance & polymorphism | No interfaces / method overriding in our own classes yet |
| 3 | JDBC | We use Spring Data JPA (Hibernate) instead of raw JDBC |
| 4 | Servlets & JSP | Not part of a Spring Boot app |
| 7 | Multithreading & synchronization | Only one `@Async` method so far |
| 10 | JSP objects & JavaBeans | Not applicable to this stack |
| 13 | JavaScript regex validation | We use HTML5 input types (`type="email"`, `minLength`) instead |
| 14 | DOM manipulation & timers | React updates the DOM for us |
| 16 | JSP scopes & navigation | Not applicable to this stack |
| 17 | Servlet & HTTP concepts | Handled by Spring's `@RestController` |
| 20 | Monolithic vs microservices | Discussed only, not coded |
| 24 | Advanced React (Context, useReducer) | Not used yet |
| 27 | Software engineering workflow | No unit tests yet |

## Test Users

There are no seeded accounts - open the app and use the **Register** tab to create
one. The register form asks for a role (Citizen or Municipal Officer) and the
password is hashed with BCrypt before it is saved.

> `backend-java/src/main/resources/data.sql` contains some sample rows, but Spring
> Boot does not run it automatically on MySQL, so it is only there for reference.
