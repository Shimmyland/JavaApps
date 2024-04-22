package org.example.exchangerates.client;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.dto.RatesDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CurrencyApiClient {

    @GET("v3/currencies")
    Call<CurrenciesDto> getAllCurrencies(@Header("apikey") final String apiKey);

    @GET("v3/latest")
    Call<RatesDto> getAllRates(@Header("apikey") final String apiKey,
                               @NotNull @Query("base_currency") final String baseCurrency,
                               @Nullable @Query("currencies") final String currencies,
                               @Nullable @Query("type") final String type);

}
