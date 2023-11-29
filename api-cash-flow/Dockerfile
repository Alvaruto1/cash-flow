FROM eclipse-temurin:20.0.1_9-jdk-alpine
RUN ls
RUN pwd
COPY ./target/*.jar ./app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 4500