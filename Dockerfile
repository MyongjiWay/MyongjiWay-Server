FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/myongjiway-0.0.1-SNAPSHOT.jar /app/myongjiway-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/myongjiway-0.0.1-SNAPSHOT.jar"]