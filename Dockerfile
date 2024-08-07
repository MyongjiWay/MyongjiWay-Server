FROM openjdk:21-jdk-slim

ARG JAR_FILE=core/core-api/build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-jar", "/app.jar"]