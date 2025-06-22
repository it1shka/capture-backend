#First stage - gradle
FROM gradle:8.0.2-jdk19 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle/ gradle/

RUN ./gradlew --version
COPY . .

RUN ./gradlew clean build -x test

#Second stage - java
FROM openjdk:19-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8088

CMD ["java", "jar", "app.jar"]