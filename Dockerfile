FROM openjdk:21-slim

COPY . /app/rsocket_client

WORKDIR /app/rsocket_client

RUN ./gradlew build

RUN cp build/libs/rsocket_client-*.jar /app/rsocket_client.jar

WORKDIR /app

CMD ["java", "-jar", "rsocket_client.jar"]
