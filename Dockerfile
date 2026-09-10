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
ENTRYPOINT java -Dspring.datasource.url=$SPRING_DATASOURCE_URL -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD -jar app.jar
