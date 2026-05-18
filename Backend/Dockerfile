# Esto es solo para modo DESARROLLO
FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY src ./src

# Poner dependencias en caché para evitar que se descarguen cada vez
RUN mvn dependency:go-offline

ENV SPRING_DEVTOOLS_RESTART_ENABLED=true

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]