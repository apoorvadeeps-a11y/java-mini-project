# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY ["gdgc air project/backend-java/mvnw", "./"]
COPY ["gdgc air project/backend-java/.mvn", "./.mvn"]
COPY ["gdgc air project/backend-java/pom.xml", "./"]
COPY ["gdgc air project/backend-java/src", "./src"]
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/airwatch-app-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080 10000
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
