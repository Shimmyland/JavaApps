package org.example.weatherapp.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.weatherapp.models.Weather;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSearchDTO {

    // used as a response of search endpoint

    private List<Weather> results = new ArrayList<>();



    // class methods
    public void add(Weather weather) {
        this.results.add(weather);
    }
}


