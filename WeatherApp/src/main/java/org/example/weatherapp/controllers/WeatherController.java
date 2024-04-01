package org.example.weatherapp.controllers;

import org.example.weatherapp.models.DTOs.*;
import org.example.weatherapp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class WeatherController {

    // dependencies
    private final WeatherService weatherService;

    // constructor
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    // endpoints
    @PostMapping("/weather")    // weather API search
    public ResponseEntity<WeatherResponseDTO> getLocalWeather(@RequestBody WeatherInputDTO weatherInputDTO) throws Exception {
        WeatherResponseDTO weatherResponseDTO = weatherService.findWeather(weatherInputDTO.getCity());
        if (weatherResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }

        weatherService.save(weatherResponseDTO, weatherInputDTO.getUserId());
        return ResponseEntity.ok(weatherResponseDTO);
    }

    @PostMapping("/search")     // weather search in DB
    public ResponseEntity<?> searchInDatabase(@RequestParam (required = false, defaultValue = "") String query){
        WeatherSearchDTO weatherSearchDTO = weatherService.searchForSavedWeathers(query);
        if (weatherSearchDTO.getResults().isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDTO("Weather with this query not found."));
        } else {
            return ResponseEntity.ok(weatherSearchDTO);
        }
    }
}
