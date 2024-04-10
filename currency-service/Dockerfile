FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar currency-service.jar
COPY src/main/resources/application.yml /usr/src/application.yml

ENTRYPOINT ["java", "-jar", "currency-service.jar"]