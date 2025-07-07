# Multi-stage build for Spring Boot application
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/taphoa-keyt-0.0.1-SNAPSHOT.jar app.jar

# Create a non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Change ownership of the app directory
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application with production profile
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"] 