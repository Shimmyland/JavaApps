package org.example.exchangerates.client;

import jakarta.annotation.Nullable;
import org.example.exchangerates.dto.ListOfCurrenciesDto;
import org.example.exchangerates.dto.ListOfRatesDto;
import org.example.exchangerates.dto.StatusDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ClientApi {

    @GET("v3/currencies")
    Call<ListOfCurrenciesDto> getAllCurrencies(@Header("apikey") String apiKey);

    @GET("v3/latest")
    Call<ListOfRatesDto> getAllRates(@Header("apikey") String apiKey,
                                     @Query("base_currency") String baseCurrency,
                                     @Nullable @Query("currencies") String currencies,
                                     @Nullable @Query("type") String type);

    @GET("v3/status")
    Call<StatusDto> getStatus(@Header("apikey") String apiKey);
}
