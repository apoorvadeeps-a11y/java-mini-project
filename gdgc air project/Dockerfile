# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY "backend-java/mvnw" .
COPY "backend-java/.mvn" .mvn
COPY "backend-java/pom.xml" .
COPY "backend-java/src" src
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/airwatch-app-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
