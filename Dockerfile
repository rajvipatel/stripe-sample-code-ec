# Multi-stage build for Stripe Connect sample app

# Stage 1: Build the Java backend
FROM maven:3.8.6-openjdk-8 AS java-builder

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy Java source code
COPY src ./src

# Build the Java application
RUN mvn clean package -DskipTests

# Stage 2: Build the React frontend
FROM node:18-alpine AS frontend-builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy frontend source code
COPY public ./public
COPY src/App.css ./src/
COPY src/App.jsx ./src/
COPY src/index.js ./src/

# Build the frontend
RUN npm run build

# Stage 3: Runtime image
FROM openjdk:8-jre-alpine

WORKDIR /app

# Install Node.js for any runtime dependencies
RUN apk add --no-cache nodejs npm

# Copy the built Java JAR from the first stage
COPY --from=java-builder /app/target/sample-jar-with-dependencies.jar ./app.jar

# Copy the built frontend from the second stage
COPY --from=frontend-builder /app/dist ./dist

# Create a startup script
RUN echo '#!/bin/sh' > start.sh && \
    echo 'java -cp app.jar com.stripe.sample.Server' >> start.sh && \
    chmod +x start.sh

# Expose the port that the Java server runs on
EXPOSE 4242

# Set environment variables for Render
ENV PORT=4242

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:4242/ || exit 1

# Start the application
CMD ["./start.sh"]
