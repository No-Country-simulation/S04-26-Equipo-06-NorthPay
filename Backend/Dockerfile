# Construcción (Build)
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Imagen de ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Creación de Usuario no-root por seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]