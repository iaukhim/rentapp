FROM eclipse-temurin:17-jdk-alpine
WORKDIR ./app
ARG JAR_FILE=rentapp-0.0.1-SNAPSHOT.jar
COPY ./target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]