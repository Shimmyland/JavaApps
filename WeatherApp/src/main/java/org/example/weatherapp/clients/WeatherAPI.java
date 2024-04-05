package org.example.weatherapp.clients;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponseDTO> weatherRequest(@Query("q") String city, @Query("appid") String apiKey);
}
