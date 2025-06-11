# Starting point
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set up working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build an app
RUN mvn clean package -DskipTests

# JDK 21 ready image
FROM eclipse-temurin:21-jdk

# Set up working directory
WORKDIR /app

# Copy .jar from previous phase
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Default starting commands
ENTRYPOINT ["java", "-jar", "app.jar"]