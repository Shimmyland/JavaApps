package org.example.weatherapp.controllers;

import org.example.weatherapp.models.DTOs.InputDTO;
import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.example.weatherapp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    // dependencies
    private final WeatherService weatherService;

    // constructor
    @Autowired
    public ApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // endpoints
    @PostMapping("/weather")
    public ResponseEntity<WeatherResponseDTO> getLocalWeather(@RequestBody InputDTO inputDTO) throws Exception {
        WeatherResponseDTO weatherResponseDTO = weatherService.findWeather(inputDTO.getCity());
        if (weatherResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }

        weatherService.save(weatherResponseDTO, inputDTO.getId());
        return ResponseEntity.ok(weatherResponseDTO);
    }
}
