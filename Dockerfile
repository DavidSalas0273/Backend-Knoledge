FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Dependencias y wrapper de Maven
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn

# Copiamos el código fuente
COPY src src

# Construimos el JAR (sin tests)
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Imagen de runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos el JAR generado
COPY --from=builder /app/target/*.jar app.jar

# Railway usa el puerto 8081 (sobrescribe con PORT)
EXPOSE 8081

# Spring Boot usa el puerto que Railway le inyecta con la variable PORT
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8081}", "-jar", "app.jar"]
