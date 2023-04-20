FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar node.jar
ENTRYPOINT ["java","-jar","/node.jar"]