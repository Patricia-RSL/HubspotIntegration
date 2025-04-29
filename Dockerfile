# Usa a imagem base do Gradle com JDK 17
FROM gradle:8.2-jdk17-alpine AS builder

# Define o diretório de trabalho para o projeto
WORKDIR /api

# Copy the .env file
COPY .env /api/.env

# Copy Gradle wrapper, build.gradle, settings.gradle, and source code
COPY gradlew ./
COPY gradle ./gradle/
COPY build.gradle settings.gradle ./

# Ensure the Gradle wrapper has execution permissions
RUN chmod +x gradlew

# Cache dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src/

# Build the project
RUN ./gradlew clean build --no-daemon --stacktrace

# Use the OpenJDK 17 base image for running the application
FROM openjdk:17-jdk-alpine

# Set the working directory for the application
WORKDIR /api

# Copy the JAR file from the builder stage
COPY --from=builder /api/build/libs/HubspotIntegration-0.0.1-SNAPSHOT.jar ./api.jar

# Expose the port where the application will run
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "api.jar"]