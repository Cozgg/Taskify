FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/Taskify-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
