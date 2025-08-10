FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/library-0.0.1-SNAPSHOT.jar /app/library.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "library.jar"]