package org.example.weatherapp.services;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;

import java.util.UUID;

public interface WeatherService {

    WeatherResponseDTO findWeather(String city);

    void save(WeatherResponseDTO weatherResponseDTO, UUID id) throws Exception;
}
