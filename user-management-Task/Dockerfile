FROM openjdk:17-jdk

WORKDIR /app

COPY target/user-management-task.jar /app/user-management-task.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "user-management-task.jar"]
