package org.example.weatherapp.services;

import org.example.weatherapp.models.DTOs.WeatherResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponseDTO> weatherRequest(@Query("q") String city, @Query("appid") String apiKey);
}
