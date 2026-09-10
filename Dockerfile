# Step 1: Build the application using Maven
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Step 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/itsutraa-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:mysql://mysql-1efb2c37-itsutraa.h.aivencloud.com:21217/defaultdb?sslMode=REQUIRED", "-Dspring.datasource.username=avnadmin", "-Dspring.datasource.password=AVNS_vqZrXMQcGXXDvAwsZKE", "-jar", "app.jar"]
