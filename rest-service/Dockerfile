FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar rest-service.jar
ENTRYPOINT ["java","-jar","/rest-service.jar"]