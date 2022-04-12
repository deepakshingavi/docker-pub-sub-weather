package com.dp.practice.subscriber;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class WeatherJsonParserTest {


    @Test
    public void testParseJsonStr() {
    String inputJson = "{" +
            "\"weather_data.date_time\":\"2019-06-04 14:05:13\"," +
            "\"weather_data.city\":\"Zurich\"," +
            "\"weather_data.currently_apparenttemperature\":29.09," +
            "\"weather_data.currently_humidity\":0.43," +
            "\"weather_data.currently_precipintensity\":0.0305," +
            "\"weather_data.currently_precipprobability\":0.01," +
            "\"weather_data.currently_preciptype\":\"rain\"," +
            "\"weather_data.currently_temperature\":29.09," +
            "\"weather_data.currently_visibility\":10.12," +
            "\"weather_data.currently_windspeed\":1.94" +
            "},";
        Weather weather = new WeatherJsonParser().readJsonStr(inputJson);
        Assert.assertNotNull(weather);
    }

    @Test
    public void testParseJsonStr1() {
        String inputJson = "{" +
                "\"weather_data.date_time\":\"2019-06-04 14:05:13\"," +
                "\"weather_data.city\":\"Zurich\"," +
                "\"weather_data.currently_apparenttemperature\":29.09," +
                "\"weather_data.currently_humidity\":0.43," +
                "\"weather_data.currently_precipintensity\":0.0305," +
                "\"weather_data.currently_precipprobability\":0.01," +
                "\"weather_data.currently_preciptype\":\"rain\"," +
                "\"weather_data.currently_temperature\":29.09," +
                "\"weather_data.currently_visibility\":10.12," +
                "\"weather_data.currently_windsped\":1.94" +
                "},";
        Weather weather = new WeatherJsonParser().readJsonStr(inputJson);
        Assert.assertNotNull(weather);
    }

    @Test
    public void testPreCIPTypeDefaultValue() {
        String inputJson = "{" +
                "\"weather_data.date_time\":\"2019-06-04 14:05:13\"," +
                "\"weather_data.city\":\"Zurich\"," +
                "\"weather_data.currently_apparenttemperature\":29.09," +
                "\"weather_data.currently_humidity\":0.43," +
                "\"weather_data.currently_precipintensity\":0.0305," +
                "\"weather_data.currently_precipprobability\": null ," +
                "\"weather_data.currently_preciptype\":null," +
                "\"weather_data.currently_temperature\":29.09," +
                "\"weather_data.currently_visibility\":10.12," +
                "\"weather_data.currently_windspeed\":1.94" +
                "},";
        Weather weather = new WeatherJsonParser().readJsonStr(inputJson);
        Assert.assertEquals(weather.getCurrently_preciptype(),WeatherJsonParser.UNKNOWN);
    }

}
