FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

# Railway usa el puerto de la app (8081)
EXPOSE 8081

# Spring Boot usa el puerto que Railway le inyecta con la variable PORT
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8081}", "-jar", "app.jar"]
