package org.example.weatherapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.weatherapp.deserializer.WeatherResponseDtoDeserializer;

@JsonDeserialize(using = WeatherResponseDtoDeserializer.class)
public record WeatherResponseDto(String city, String country, String forecast, double temp) {
}