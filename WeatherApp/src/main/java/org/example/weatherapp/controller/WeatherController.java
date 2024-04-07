package org.example.weatherapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.weatherapp.dto.WeatherInputDTO;
import org.example.weatherapp.dto.WeatherResponseDTO;
import org.example.weatherapp.dto.WeatherListDTO;
import org.example.weatherapp.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/weathers")
@RequiredArgsConstructor
// @Validated
public class WeatherController {

    private final WeatherService weatherService;

    // @SneakyThrows
    @PostMapping("/search")
    public ResponseEntity<WeatherResponseDTO> searchForWeather(@Valid @RequestBody final WeatherInputDTO weatherInputDTO) {
        return ResponseEntity.ok(weatherService.createNewWeatherForecast(weatherInputDTO.city()));
    }

    @GetMapping("/search")
    public ResponseEntity<WeatherListDTO> listWeatherFromDatabase(@RequestParam(required = false, defaultValue = "") final String city){
        return ResponseEntity.ok(weatherService.getWeatherBy(city));
    }


    /* RestTemplate - example
    private final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid=" + System.getenv().get("apiKey");

    @GetMapping
    public WeatherResponseDTO getWeather(final String city) {
        RestTemplate restTemplate = new RestTemplate();
        String request = API_REQUEST_URL.replace("{city}", city);
        return restTemplate.getForObject(request, WeatherResponseDTO.class);
    }
    */
}
