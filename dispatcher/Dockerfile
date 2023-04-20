FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar dispatcher.jar
ENTRYPOINT ["java","-jar","/dispatcher.jar"]