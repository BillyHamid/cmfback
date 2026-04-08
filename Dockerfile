# Build JAR inside the image (CI n'a pas de dossier target/ pré-compilé)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src src

RUN chmod +x mvnw \
  && sed -i 's/\r$//' mvnw \
  && ./mvnw -B package

# Image d'exécution légère
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# Config embarquée dans le JAR + surcharge par variables d'environnement (Railway, etc.)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
