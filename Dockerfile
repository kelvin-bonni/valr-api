# Step 1: Build the Gradle project
FROM gradle:8.8-jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle project files to the container
COPY . .

# Build the project using Gradle and create the WAR file
RUN gradle clean build

# Step 2: Use a JDK base image to run the WAR file
FROM openjdk:17

# Set the name of the WAR file
ARG WAR_FILE=build/libs/valr-api-*.war

# Copy the WAR file from the Gradle build to the container
COPY --from=build /app/${WAR_FILE} /app/valr-api.war

# Set the working directory
WORKDIR /app

# Expose port 8080
EXPOSE 8080

# List the WAR files in the directory
RUN ls -lh /app/*.war

# Run the WAR file
CMD ["java", "-jar", "valr-api.war"]