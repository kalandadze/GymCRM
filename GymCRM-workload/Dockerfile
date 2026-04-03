FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/GymCRM-workload-0.0.1-SNAPSHOT.jar app.jar

ENV DB_ENABLED=false
ENV QUEUE_ENABLED=false
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]