package com.dp.practice.subscriber;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class WeatherSubscriberApp implements CommandLineRunner {

    @Autowired
    private WeatherJsonParser jsonParser = null;

    public static void main(String[] args) {
        SpringApplication.run(WeatherSubscriberApp.class, args);
    }

    @Autowired
    WeatherDAO jdbcConfig;

    @Override
    public void run(String... args) {

        String bootstrapServer;
        List<String> topics;
        String groupId;

        if (args.length < 3) {
            log.error("java -jar target/weather-subscriber <bootstrap_server> <group_id> <topics>");
            log.error("Example: java -jar target/weather-subscriber localhost:29092 my-group weather_topic");
            System.exit(1);
        }

        bootstrapServer = args[0];
        groupId = args[1];
        topics = Arrays.asList(args[2].split(","));

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", groupId);

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(topics);
        try {
            while (true) { //
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

                List<Weather> insertWeatherRecords = new LinkedList<>();
                for (ConsumerRecord<String,String> record : records) {
                    String weatherJsonStr = record.value();
                    Weather weather = jsonParser.readJsonStr(weatherJsonStr);
                    if(null!=weather){
                        insertWeatherRecords.add(weather);
                    }
                }
                jdbcConfig.batchInsertWeatherObjectAsync(insertWeatherRecords);
            }
        } catch (DataAccessException e) {
            log.error("Error while accessing database",e);
        } finally {
            kafkaConsumer.close();
        }
    }
}