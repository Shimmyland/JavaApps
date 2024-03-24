package org.example.weatherapp.services;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;

public interface WeatherService {

    WeatherResponseDTO findWeather(String city);

    void save(WeatherResponseDTO weatherResponseDTO, Long id) throws Exception;
}
