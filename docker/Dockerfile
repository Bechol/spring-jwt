#Build jar
FROM maven:3.6.0-jdk-11-slim AS build
COPY ../src /home/app/src
COPY ../pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#Build image
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/spring-jwt-app-0.0.1-SNAPSHOT.jar /usr/local/lib/spring-jwt-app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/spring-jwt-app.jar"]

#Environment variables
#APP_LOCALE
#JDBC_DRIVER_NAME
#DATABASE_URL (example jdbc:postgresql://localhost:5050/jwtdemo)
#JDBC_DATABASE_USERNAME
#JDBC_DATABASE_PASSWORD
#MAIL_PROTOCOL
#MAIL_HOST
#MAIL_PORT
#MAIL_USERNAME
#MAIL_PASSWORD
#JWT_SECRET
#JWT_TOKEN_EXPIRATION (ms)
