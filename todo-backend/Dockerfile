FROM openjdk:17-alpine
COPY build/libs/*.jar assignment.jar
ENTRYPOINT ["java","-jar","/assignment.jar"]
EXPOSE 8080