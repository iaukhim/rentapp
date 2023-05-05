FROM eclipse-temurin:17-jdk-alpine
WORKDIR ./app
ARG JAR_FILE=./target/rentapp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djavax.net.ssl.trustStore=/certificates/rent-app-truststr.p12", \
                   "-Djavax.net.ssl.trustStorePassword=password", \
                   "-Djavax.net.ssl.keyStore=/certificates/rent-app-keystr.p12", \
                   "-Djavax.net.ssl.keyStorePassword=password", \
                   "-Djavax.net.debug=all", \
            "-jar","/app/app.jar"]