FROM openjdk:21-jre-slim

WORKDIR /app

COPY build/libs/myongjiway.jar /app/myongjiway.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app/myongjiway.jar"]