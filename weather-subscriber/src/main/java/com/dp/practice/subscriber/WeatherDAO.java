package com.dp.practice.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Configuration
@Component
public class WeatherDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${jdbc.batch_insert_size}")
    private int batchSize;

    private static final String WEATHER_INERT_QUERY = "INSERT INTO WEATHER " +
            "(city, currently_apparenttemperature, currently_humidity, " +
            "currently_precipintensity, currently_precipprobability, currently_preciptype, " +
            "currently_temperature, currently_visibility, currently_windspeed, " +
            "date_time)"
            + " VALUES (?,?,?,?,?,?,?,?,?,?)";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public int[][] batchInsertWeatherObjectAsync(List<Weather> weatherObjects) {

        int[][] updateCounts = jdbcTemplate.batchUpdate(
                WEATHER_INERT_QUERY,
                weatherObjects,
                batchSize,
                (ps, weather) -> {
                    ps.setString(1, weather.getCity());
                    ps.setDouble(2, weather.getCurrently_temperature());
                    ps.setDouble(3, weather.getCurrently_humidity());
                    ps.setDouble(4, weather.getCurrently_precipintensity());
                    ps.setDouble(5, weather.getCurrently_precipprobability());
                    ps.setString(6, weather.getCurrently_preciptype());
                    ps.setDouble(7, weather.getCurrently_temperature());
                    ps.setString(8, weather.getCurrently_visibility());
                    ps.setDouble(9, weather.getCurrently_windspeed());
                    ps.setTimestamp(10, new Timestamp(weather.getDate_time().getTime()));
                });

        return updateCounts;
    }
}
