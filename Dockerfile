# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Copy only what Maven needs to build
COPY pom.xml .
COPY src ./src

# Build the shaded JAR (skip tests for faster builds)
RUN mvn -DskipTests clean package

# Validation: list jars to confirm build output
RUN ls -la target/*.jar

# ---- runtime stage ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the shaded uber-jar (shade plugin produces wordle-1.0-SNAPSHOT.jar)
COPY --from=build /app/target/wordle-*.jar app.jar

# Render sets PORT env var; our app reads it (default 7070)
EXPOSE 7070

CMD ["java", "-jar", "app.jar"]