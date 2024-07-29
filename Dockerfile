FROM openjdk:21-jre-slim

ARG JAR_FILE=core/core-api/build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]