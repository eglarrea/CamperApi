# Etapa 1: Construcción del proyecto
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY go/ /app
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con el .jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
