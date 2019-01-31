FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
ARG APP_DIR=/opt/app
RUN mkdir ${APP_DIR}
WORKDIR ${APP_DIR}
ARG APP_JAR=quantasma-app-0.0.1-SNAPSHOT.jar
COPY target/${APP_JAR} quantasma-app.jar
ENTRYPOINT ["java", "-jar", "quantasma-app.jar"]
EXPOSE 8080