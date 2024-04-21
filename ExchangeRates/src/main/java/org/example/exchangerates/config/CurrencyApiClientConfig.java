package org.example.exchangerates.config;

import org.example.exchangerates.client.ClientApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class ClientApiConfig {

    @Bean
    public ClientApi clientApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.currencyapi.com/")             // https://fixer.io/documentation
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(ClientApi.class);
    }

    // url do properties nebo nekam also ten name je obecnej (setApi?)
}
