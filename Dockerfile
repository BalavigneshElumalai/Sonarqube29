# Use the official Maven image as the base image
FROM maven:3.9.9-eclipse-temurin-21

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml to the container
COPY pom.xml .

# Copy the source code to the container
COPY src ./src

# Build the application using Maven
RUN mvn package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/ecommerce-app-1.0-SNAPSHOT.jar"] 