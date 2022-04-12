FROM openjdk:8-jdk-alpine

ENV KAFKA_BOOTSTRAP_SERVER ""
ENV JSON_SRC ""
ENV TOPIC ""

ADD weather.json.gz /weather.json.gz

ADD weather-publisher*.jar /weather-publisher.jar

ENTRYPOINT java -jar /weather-publisher.jar $KAFKA_BOOTSTRAP_SERVER $JSON_SRC $TOPIC
