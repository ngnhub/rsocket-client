FROM openjdk:21-slim

COPY ./build/libs/rsocket_client-*.jar /app/rsocket_client.jar

WORKDIR /app

CMD ["java", "-jar", "rsocket_client.jar"]
