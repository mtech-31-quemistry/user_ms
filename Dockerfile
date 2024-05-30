# Custom Java runtime using jlink in a multi-stage container build
FROM eclipse-temurin:21 as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules jdk.unsupported,java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Define your base image
FROM debian:stretch-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

FROM gradle:8.7-jdk21 AS build
COPY . .

RUN gradle clean bootJar

RUN mv build/libs/user_ms.jar user_ms.jar
RUN mv src/main/resources/application.yml application.yml
EXPOSE 8080
CMD ["java", "-jar", "user_ms.jar"]
