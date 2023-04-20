FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar common-jpa.jar
ENTRYPOINT ["java","-jar","/common-jpa.jar"]