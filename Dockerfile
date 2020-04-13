FROM maven:latest
COPY ./ /app
WORKDIR /app/
RUN mvn clean build
COPY target/*.jar /app.jar
