package org.example.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.weatherapp.entity.Weather;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class WeatherListDto {

    private List<WeatherDto> result = new ArrayList<>();

    public void add(Weather weather) {
        WeatherDto weatherDTO = new WeatherDto(
                weather.getCreateAt(),
                weather.getCity(),
                weather.getCountry(),
                weather.getTemperature(),
                weather.getForecast()
        );
        this.result.add(weatherDTO);
    }

    @Data
    @AllArgsConstructor
    public static class WeatherDto {
        private LocalDateTime createAt;
        private String city;
        private String country;
        private double temperature;
        private String weather;
    }
}


