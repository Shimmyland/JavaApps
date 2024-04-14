package org.example.weatherapp.config;

import org.example.weatherapp.client.WeatherAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class ClientApiConfig {

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Bean
    public WeatherAPI weatherAPI(Retrofit retrofit) {
        return retrofit.create(WeatherAPI.class);
    }
}
