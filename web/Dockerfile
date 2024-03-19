FROM openjdk:21-slim

COPY ./build/libs/web.jar /app/rsocket_client.jar

WORKDIR /app

CMD ["java", "-jar", "rsocket_client.jar"]
