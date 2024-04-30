FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY analytics-service/pom.xml analytics-service/pom.xml
COPY analytics-service/src analytics-service/src

RUN mvn -f analytics-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk@sha256:dd90127baca82663c56ff903156d229a8287fe94a7b0145a31b45fecebdb7be6

WORKDIR /app

COPY --from=build /build/analytics-service/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
