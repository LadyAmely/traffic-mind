ARG IMAGE_VERSION=21.0.7_6

FROM eclipse-temurin:${IMAGE_VERSION}-jdk-alpine AS builder

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
COPY checkstyle.xml ./checkstyle.xml
COPY checkstyle-suppressions.xml ./checkstyle-suppressions.xml

RUN ./gradlew clean build -PskipIntegrationTests --no-daemon

FROM eclipse-temurin:${IMAGE_VERSION}-jre AS runtime
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar ./app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s \
    CMD curl -f http://localhost:8080/actuator.health // exit 1

RUN addgroup --system app && adduser --system --group app
USER app

ENTRYPOINT ["java", "-jar", "app.jar"]