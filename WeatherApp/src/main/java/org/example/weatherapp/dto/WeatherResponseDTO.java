package org.example.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {

    private Long id;
    private String name;
    private int cod;
    private Coord coord;
    private Weather[] weather;
    private Main main;
    private int visibility;
    private Sys sys;

    @Data
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }

    @Data
    public static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Sys {
        private String country;
        private long sunrise;
        private long sunset;
    }
}