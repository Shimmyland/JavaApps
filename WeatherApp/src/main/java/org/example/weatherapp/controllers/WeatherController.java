package org.example.weatherapp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.models.DTOs.WeatherInputDTO;
import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.example.weatherapp.models.DTOs.WeatherListDTO;
import org.example.weatherapp.services.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Validated
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/search")
    public ResponseEntity<WeatherResponseDTO> searchForWeather(@Valid @RequestBody WeatherInputDTO weatherInputDTO) throws IOException {
        return ResponseEntity.ok(weatherService.createNewWeatherForecast(weatherInputDTO));
    }

    @GetMapping("/search")
    public ResponseEntity<WeatherListDTO> listWeatherFromDatabase(@RequestParam(required = false, defaultValue = "") UUID userId){
        return ResponseEntity.ok(weatherService.getSavedForecast(userId));
    }

}
