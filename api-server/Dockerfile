# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar

# Run stage
FROM eclipse-temurin:17-jdk-alpine AS runner

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY src/main/resources/firebase/b-10221-seminar-firebase-adminsdk-fbsvc-955d54a49d.json /app/

ENV FIRESTORE_TRANSPORT=rest

CMD ["java", "-jar", "app.jar"]

