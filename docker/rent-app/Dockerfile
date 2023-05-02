FROM eclipse-temurin:17-jdk-alpine
WORKDIR ./app
ARG JAR_FILE=./target/rentapp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]