# Use a base image with Java 19
FROM openjdk:latest

# Set the working directory in the container
WORKDIR /app

# Copy the compiled class files to the container
COPY build/libs /app

# Set the entry point to run the Java application
ENTRYPOINT ["java", "-jar", "/app/xcore-latest-fat.jar", "-conf", "/etc/vertx/config.json"]
