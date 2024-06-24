# Dockerfile

# jdk17 Image Start
FROM openjdk:17

ARG JAR_FILE=build/libs/Vacation_CICD_Practice-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} vacation_cicd.jar
ENTRYPOINT ["java","-jar","/vacation_cicd.jar"]