package org.example.exchangerates.client;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.dto.RatesDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ClientApi {

    // private static final String API_KEY = System.getenv().get("apiKey");

    @GET("v3/currencies")
    Call<CurrenciesDto> getAllCurrencies(@Header("apikey") String apiKey);

    @GET("v3/latest")
    Call<RatesDto> getAllRates(@Header("apikey") String apiKey,
                               @NotNull @Query("base_currency") String baseCurrency,
                               @Nullable @Query("currencies") String currencies,
                               @Nullable @Query("type") String type);

    // code overloading - nefunguje mi to, potřebuju dvě varianty, kdy mám vždy dvě query
}
