package org.example.weatherapp.services.implementations;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.example.weatherapp.models.Weather;
import org.example.weatherapp.repositories.WeatherRepository;
import org.example.weatherapp.services.UserService;
import org.example.weatherapp.services.WeatherAPI;
import org.example.weatherapp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class WeatherServiceImp implements WeatherService {


    // dependencies
    private final WeatherRepository weatherRepository;
    private final UserService userService;

    // fields
    private WeatherAPI weatherAPI;
    private final String apiKey = System.getenv().get("key");

    // constructor
    @Autowired
    public WeatherServiceImp(WeatherRepository weatherRepository, UserService userService) {
        this.weatherRepository = weatherRepository;
        this.userService = userService;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.weatherAPI = retrofit.create(WeatherAPI.class);
        System.out.println("apikey: " + apiKey);
    }

    // methods
    @Override
    public WeatherResponseDTO findWeather(String city) {
        Call<WeatherResponseDTO> request = weatherAPI.weatherRequest(city, apiKey);
        WeatherResponseDTO result = null;

        try {
            Response<WeatherResponseDTO> getResponse = request.execute();
            if (getResponse.isSuccessful() && getResponse.body() != null){
                result = getResponse.body();
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        // save weather into DB?

        return result;
    }

    @Override
    public void save(WeatherResponseDTO weatherResponseDTO, Long id) throws Exception {
        String weatherCondition = null;
        for (WeatherResponseDTO.Weather weather : weatherResponseDTO.getWeather()){
            weatherCondition = weather.getMain();
        }

        Weather weather = new Weather(
                userService.find(id),
                weatherResponseDTO.getName(),
                weatherResponseDTO.getSys().getCountry(),
                weatherResponseDTO.getMain().getTemp(),
                weatherCondition
        );

        weatherRepository.save(weather);
    }
}
