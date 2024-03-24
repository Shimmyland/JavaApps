package org.example.weatherapp.models.DTOs;

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
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private long dt;
    private Sys sys;

    // Nested classes to represent the nested JSON objects
    @Data
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Wind {
        private double speed;
        private int deg;
    }

    @Data
    public static class Rain {
        private double h1;
    }

    @Data
    public static class Clouds {
        private int all;
    }

    @Data
    public static class Sys {
        private int type;
        private int id;
        private String country;
        private long sunrise;
        private long sunset;
    }
}