FROM eclipse-temurin:17-jdk-jammy

ARG JAR_PATH
COPY ${JAR_PATH} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
