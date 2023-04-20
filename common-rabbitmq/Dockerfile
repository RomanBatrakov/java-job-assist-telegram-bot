FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar common-rabbitmq.jar
ENTRYPOINT ["java","-jar","/common-rabbitmq.jar"]