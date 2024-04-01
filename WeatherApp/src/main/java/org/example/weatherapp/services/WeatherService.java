package org.example.weatherapp.services;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.example.weatherapp.models.DTOs.WeatherSearchDTO;
import org.example.weatherapp.models.Weather;

import java.util.List;
import java.util.UUID;

public interface WeatherService {

    WeatherResponseDTO findWeather(String city);

    void save(WeatherResponseDTO weatherResponseDTO, UUID userId) throws Exception;

    WeatherSearchDTO searchForSavedWeathers(String query);

}
