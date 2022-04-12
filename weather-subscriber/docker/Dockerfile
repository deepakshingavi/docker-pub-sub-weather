FROM openjdk:8-jdk-alpine

ENV KAFKA_BOOTSTRAP_SERVER ""
ENV CONSUMER_GROUP_ID ""
ENV TOPIC ""

ADD weather-subscriber*.jar /weather-subscriber.jar

ENTRYPOINT java -jar /weather-subscriber.jar $KAFKA_BOOTSTRAP_SERVER $CONSUMER_GROUP_ID $TOPIC
