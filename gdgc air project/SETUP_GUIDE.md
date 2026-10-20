# AirWatch - Setup Guide

Two parts: a Spring Boot backend and a React frontend. The backend stores
everything in MySQL.

## Prerequisites

- Java 17 or higher
- Maven
- MySQL 8
- Node.js 18 or higher with npm

Check them with:

```bash
java -version
mvn -version
mysql --version
node -v
npm -v
```

## Part 1: Database

Create the database:

```sql
CREATE DATABASE airwatch_db;
```

The app builds the tables itself on first start (`spring.jpa.hibernate.ddl-auto=update`),
so you do not need to create them by hand.

## Part 2: Backend

1. Open `backend-java/src/main/resources/application.properties` and set your own
   MySQL username and password:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

2. Run it:

```bash
cd backend-java
mvn spring-boot:run
```

The backend starts on `http://localhost:8080/api`.

3. Check it is up:

```bash
curl http://localhost:8080/api/users
```

You should get `[]` (or a list of users if you already registered some).

## Part 3: Frontend

Open a **new terminal**:

```bash
cd frontend
npm install
npm run dev
```

It starts on `http://localhost:5173`. Open that in your browser.

## Part 4: Try It Out

1. **Register** tab - create an account (pick a role, set a password)
2. **Login** tab - log in with the same details
3. **Submit Report** - write a description, click *Get Location* to fill the
   coordinates, pick a severity from 1 to 5, then submit
4. **My Reports** - the report you submitted appears here with status PENDING

## Part 5: API Testing (optional)

```bash
# Register
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"test123\",\"fullName\":\"Test User\",\"role\":\"CITIZEN\"}"

# Login
curl -X POST http://localhost:8080/api/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"test123\"}"

# Create a report
curl -X POST http://localhost:8080/api/reports ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":1,\"description\":\"Burning garbage near the park\",\"latitude\":19.0760,\"longitude\":72.8777,\"locationName\":\"Andheri, Mumbai\",\"severity\":4}"

# Read reports
curl http://localhost:8080/api/reports
```

> On Linux/macOS use `\` instead of `^` to continue a line.

## Project Structure

```
gdgc air project/
├── backend-java/
│   ├── src/main/java/com/airwatch/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Database access
│   │   ├── model/               # JPA entities
│   │   ├── dto/                 # Request/response objects
│   │   ├── exception/           # Custom exceptions
│   │   ├── config/              # CORS and async config
│   │   └── util/                # AQI calculator, email helper
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── data.sql
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── App.jsx              # Main component
    │   ├── App.css              # Styling
    │   ├── main.jsx             # Entry point
    │   └── index.css            # Global styles
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## Troubleshooting

**Port 8080 already in use** - change `server.port` in `application.properties`.

**MySQL connection refused** - make sure MySQL is running and that the username
and password in `application.properties` are correct.

**Port 5173 already in use** - Vite will offer the next free port; use whatever
it prints in the terminal.

**Frontend cannot reach the backend** - check the backend is running on port
8080. The frontend's API address is the `API_BASE` constant at the top of
`frontend/src/App.jsx`.
