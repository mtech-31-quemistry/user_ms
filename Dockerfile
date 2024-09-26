# Build and Generate Jar Stage
FROM gradle:8.7.0-jdk21 AS project-build
COPY . .

RUN gradle bootJar

RUN mv build/libs/user_ms.jar /usr/share/user_ms.jar
RUN mv src/main/resources/application.yml /usr/share/application.yml
RUN mv src/main/resources/application-prod.yml /usr/share/application-prod.yml
RUN mv src/main/resources/email /usr/share/email


# Custom Java runtime using jlink in a multi-stage container build
FROM eclipse-temurin:21 as jre-build
# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules jdk.unsupported,java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.crypto.ec \
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

COPY --from=project-build /usr/share/user_ms.jar .
COPY --from=project-build /usr/share/application.yml .
COPY --from=project-build /usr/share/application-prod.yml .
COPY --from=project-build /usr/share/email .

EXPOSE 80
CMD ["java", "-jar", "user_ms.jar"]
