FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/itsutraa-1.0.jar app.jar
RUN mkdir -p /app/uploads
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
