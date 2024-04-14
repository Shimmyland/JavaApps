package org.example.weatherapp.client;

import org.example.weatherapp.dto.WeatherResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponseDto> weatherRequest(@Query("q") String city, @Query("appid") String apiKey);
}
