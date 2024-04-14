package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.client.WeatherAPI;
import org.example.weatherapp.exception.WeatherNotFoundException;
import org.example.weatherapp.dto.WeatherResponseDto;
import org.example.weatherapp.dto.WeatherListDto;
import org.example.weatherapp.entity.Weather;
import org.example.weatherapp.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherAPI weatherAPI;
    private static final String API_KEY = System.getenv().get("apiKey");


    @Transactional
    public WeatherResponseDto createNewWeatherForecast(final String city) {
        Call<WeatherResponseDto> request = weatherAPI.weatherRequest(city, API_KEY);
        try {
            Response<WeatherResponseDto> response = request.execute();
            if (!response.isSuccessful() || response.body() == null){
                throw new WeatherNotFoundException();
            }
            WeatherResponseDto weatherResponseDto = response.body();
            weatherRepository.save(new Weather(
                    weatherResponseDto.city(),
                    weatherResponseDto.country(),
                    weatherResponseDto.temp(),
                    weatherResponseDto.forecast()
            ));
            return weatherResponseDto;
        } catch (IOException e){
            throw new WeatherNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public WeatherListDto getWeatherBy(final String city) {
        List<Weather> result = weatherRepository.findTop100ByCityContainingOrderByCreateAt(city);
        if (result == null){
            throw new WeatherNotFoundException();
        }
        WeatherListDto weatherListDTO = new WeatherListDto();
        for (Weather weather : result) {
            weatherListDTO.add(weather);
        }
        return weatherListDTO;
    }
}
