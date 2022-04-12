package com.dp.practice.subscriber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Weather {

    @JsonProperty("weather_data.city")
    private String city;

    @JsonProperty("weather_data.currently_apparenttemperature")
    private Double currently_apparenttemperature;

    @JsonProperty("weather_data.currently_humidity")
    private Double currently_humidity;

    @JsonProperty("weather_data.currently_precipintensity")
    private Double currently_precipintensity;

    @JsonProperty("weather_data.currently_precipprobability")
    private Double currently_precipprobability;

    @JsonProperty(value = "weather_data.currently_preciptype")
    private String currently_preciptype="UNKNOWN";

    @JsonProperty("weather_data.currently_temperature")
    private Double currently_temperature;

    @JsonProperty("weather_data.currently_visibility")
    private String currently_visibility;

    @JsonProperty("weather_data.currently_windspeed")
    private Double currently_windspeed;

    @JsonProperty("weather_data.date_time")
    private Date date_time;

}
