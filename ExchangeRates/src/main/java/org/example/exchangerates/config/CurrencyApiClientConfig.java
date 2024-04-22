package org.example.exchangerates.config;

import lombok.RequiredArgsConstructor;
import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class CurrencyApiClientConfig {

    private final CurrencyApiProperties currencyApiProperties;

    @Bean
    public CurrencyApiClient currencyApiClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(currencyApiProperties.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(CurrencyApiClient.class);
    }
}
