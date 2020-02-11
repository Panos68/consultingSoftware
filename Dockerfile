FROM openjdk:11-jdk-slim AS java-build

WORKDIR /app/

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package
RUN mkdir -p target/dependency && (cd target/dependency; jar xf ../*-jar-with-dependencies.jar)


# Use distroless for the run-time image
FROM gcr.io/distroless/java:11
ARG DEPENDENCY=/app/target/dependency
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=java-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-cp","app:app/lib/*","ConsultancyManagementApplication"]

