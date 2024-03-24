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
    @PostMapping("/weather")        // error 415 with @RequestBody InputDTO inputDTO and inputDTO.getCity()
    public ResponseEntity<WeatherResponseDTO> getLocalWeather(@RequestParam String city, @RequestParam Long id) throws Exception {
        WeatherResponseDTO weatherResponseDTO = weatherService.findWeather(city);
        if (weatherResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }

        weatherService.save(weatherResponseDTO, id);
        return ResponseEntity.ok(weatherResponseDTO);
    }
}
