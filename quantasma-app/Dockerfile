FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
RUN mkdir /opt/app
COPY target/quantasma-app-0.0.1-SNAPSHOT.jar /opt/app
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dukascopy", "/opt/app/quantasma-app-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080