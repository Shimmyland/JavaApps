package org.example.weatherapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.dto.InputDto;
import org.example.weatherapp.dto.WeatherResponseDto;
import org.example.weatherapp.dto.WeatherListDto;
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
public class WeatherController {

    private final WeatherService weatherService;

    // @SneakyThrows
    @PostMapping("/search")
    public ResponseEntity<WeatherResponseDto> searchForWeather(@Valid @RequestBody final InputDto inputDto) {
        return ResponseEntity.ok(weatherService.createNewWeatherForecast(inputDto.city()));
    }

    @GetMapping("/search")
    public ResponseEntity<WeatherListDto> listWeatherFromDatabase(@RequestParam(required = false, defaultValue = "") final String city){
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
