package com.dp.practice.publisher;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

@Slf4j
@SpringBootApplication
public class WeatherPublisherApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WeatherPublisherApp.class, args);
    }

    @Override
    public void run(String... args) {
        String bootstrapServer = "";
        String weatherJsonPath = null;
        String topic = null;

        if (args.length < 3) {
            log.error("java -jar target/weather-publisher <bootstrap-server> <weather-json-file-path> <topic-name>");
            log.error("Example: java -jar target/weather-publisher localhost:29092 file://weather.json weather_topic");
            System.exit(1);
        }

        try {
            bootstrapServer = args[0];
            weatherJsonPath = args[1];
            topic = args[2];
        } catch (Exception e) {
            log.error("Error occured while init the Weather Publisher", e);
            System.exit(1);
        }
        ingestJson(weatherJsonPath, topic, bootstrapServer);
    }

    public void ingestJson(String weatherJsonPath, String topic, String bootstrapServer) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "1");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        try {
            Path path = Paths.get(weatherJsonPath);
            try (
                    InputStream is = Files.newInputStream(path);
                    GZIPInputStream gis = new GZIPInputStream(is);
                    InputStreamReader isReader = new InputStreamReader(gis, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isReader);
            ) {
                String currLine;
                while (null != (currLine = br.readLine())) {
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, currLine, currLine);
                    kafkaProducer.send(record);
                }
            } catch (IOException e) {
                log.error("IOException {}", e);
            }
        } finally {
            kafkaProducer.close();
        }
    }

}

