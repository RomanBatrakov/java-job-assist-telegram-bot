FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar common-utils.jar
ENTRYPOINT ["java","-jar","/common-utils.jar"]