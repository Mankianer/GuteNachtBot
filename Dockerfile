FROM eclipse-temurin:21-jre-alpine


WORKDIR /opt/app
COPY build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
