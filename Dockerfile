#BUILD
FROM maven:3.9.6 AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests -X


#RUN
FROM openjdk:21-slim AS final

WORKDIR /app
ARG JAR_FILE=/app/target/inghub-*SNAPSHOT.jar
COPY --from=builder ${JAR_FILE} inghub-app.jar

ENTRYPOINT ["java", "-jar", "inghub-app.jar"]
EXPOSE 8080