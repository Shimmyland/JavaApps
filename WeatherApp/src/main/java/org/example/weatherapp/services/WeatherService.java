package org.example.weatherapp.services;

import org.example.weatherapp.clients.WeatherAPI;
import org.example.weatherapp.exceptions.UserNotFoundException;
import org.example.weatherapp.exceptions.WeatherNotFoundException;
import org.example.weatherapp.models.DTOs.WeatherInputDTO;
import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.example.weatherapp.models.DTOs.WeatherListDTO;
import org.example.weatherapp.models.Weather;
import org.example.weatherapp.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final UserService userService;
    private final WeatherAPI weatherAPI;
    private static final String apiKey = System.getenv().get("apiKey");

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, UserService userService) {
        this.weatherRepository = weatherRepository;
        this.userService = userService;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.weatherAPI = retrofit.create(WeatherAPI.class);
    }


    @Transactional
    public WeatherResponseDTO createNewWeatherForecast(WeatherInputDTO weatherInputDTO) throws IOException, NullPointerException, DataAccessException, WeatherNotFoundException {
        Call<WeatherResponseDTO> request = weatherAPI.weatherRequest(weatherInputDTO.city(), apiKey);
        Response<WeatherResponseDTO> getResponse = request.execute();
        WeatherResponseDTO weatherResponseDTO = getResponse.body();

        String weatherCondition = null;
        for (WeatherResponseDTO.Weather weather : weatherResponseDTO.getWeather()) {
            weatherCondition = weather.getMain();
        }

        weatherRepository.save(new Weather(
                userService.getUserById(weatherInputDTO.userId()),
                weatherResponseDTO.getName(),
                weatherResponseDTO.getSys().getCountry(),
                weatherResponseDTO.getMain().getTemp(),
                weatherCondition
        ));
        return weatherResponseDTO;
    }

    @Transactional(readOnly = true)
    public WeatherListDTO getSavedForecast(UUID userId) throws IllegalArgumentException {
        Optional<List<Weather>> tmp = weatherRepository.findByUserId(userId);
        List<Weather> result = tmp.orElseThrow(() -> new UserNotFoundException());

        WeatherListDTO weatherListDTO = new WeatherListDTO(result.get(0).getUser().getName() + " " + result.get(0).getUser().getSurname());
        for (Weather weather : result) {
            weatherListDTO.add(weather);
        }
        return weatherListDTO;
    }
}
