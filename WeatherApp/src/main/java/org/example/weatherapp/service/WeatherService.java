package org.example.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.example.weatherapp.client.WeatherAPI;
import org.example.weatherapp.exception.WeatherNotFoundException;
import org.example.weatherapp.dto.WeatherResponseDTO;
import org.example.weatherapp.dto.WeatherListDTO;
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
    private static final String apiKey = System.getenv().get("apiKey");


    @Transactional
    public WeatherResponseDTO createNewWeatherForecast(final String city) {
        Call<WeatherResponseDTO> request = weatherAPI.weatherRequest(city, apiKey);
        try {
            Response<WeatherResponseDTO> response = request.execute();
            if (!response.isSuccessful() && response.body() == null){
                throw new WeatherNotFoundException();
            }
            WeatherResponseDTO weatherResponseDTO = response.body();
            weatherRepository.save(new Weather(
                    weatherResponseDTO.getName(),
                    weatherResponseDTO.getSys().getCountry(),
                    weatherResponseDTO.getMain().getTemp(),
                    weatherResponseDTO.getWeather()[0].getMain()
            ));
            return weatherResponseDTO;
        } catch (IOException e){
            throw new WeatherNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public WeatherListDTO getWeatherBy(final String city) {
        List<Weather> result = weatherRepository.findTop100ByCityContainingOrderByCreateAt(city);
        if (result == null){
            throw new WeatherNotFoundException();
        }

        WeatherListDTO weatherListDTO = new WeatherListDTO();
        for (Weather weather : result) {
            weatherListDTO.add(weather);
        }
        return weatherListDTO;
    }
}
