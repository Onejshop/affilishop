# Etap 1: Build aplikasyon an
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Etap 2: Kouri aplikasyon an
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/affilishop-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
