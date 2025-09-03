FROM openjdk:24-ea-jdk-bookworm

LABEL authors="CorthosCode"

WORKDIR /app

COPY ./call-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]