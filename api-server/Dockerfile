# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY . .
COPY b-10221-seminar-firebase-adminsdk-fbsvc-08c90ad9b0.json /app/firebase-key.json
RUN chmod +x gradlew
RUN ./gradlew bootJar

# Run stage
FROM eclipse-temurin:17-jdk-alpine AS runner

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY b-10221-seminar-firebase-adminsdk-fbsvc-08c90ad9b0.json /app/firebase-key.json

ENV FIRESTORE_TRANSPORT=rest

CMD ["java", "-jar", "app.jar"]

