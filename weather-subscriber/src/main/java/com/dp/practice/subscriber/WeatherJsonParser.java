package com.dp.practice.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class WeatherJsonParser {

    private ObjectMapper objectMapper;
    private static final DateFormat JSON_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String UNKNOWN = "UNKNOWN";

    public WeatherJsonParser() {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(JSON_DATE_TIME_FORMAT);
    }

    public Weather readJsonStr(String jsonStr) {
        try {
            if (null != jsonStr) {
                Weather weather = objectMapper.readValue(jsonStr, Weather.class);
                dataCleanUp(weather);
                return weather;
            }
        } catch (JsonProcessingException ex) {
            log.error("Error while parsing {} to Weather object", jsonStr, ex);
        }
        return null;

    }

    private void dataCleanUp(Weather weather) {
        if(null == weather.getCurrently_preciptype() || "null".equalsIgnoreCase(weather.getCurrently_preciptype()) ) {
            weather.setCurrently_preciptype(UNKNOWN);
        }
    }

}
