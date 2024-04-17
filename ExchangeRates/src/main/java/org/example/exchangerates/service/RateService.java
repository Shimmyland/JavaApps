package org.example.exchangerates.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.exchangerates.client.ClientApi;
import org.example.exchangerates.dto.ListOfRatesDto;
import org.example.exchangerates.entity.Cur;
import org.example.exchangerates.entity.Rate;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RateService {

    private final CurrencyService currencyService;
    private final RateRepository rateRepository;
    private final ClientApi clientApi;
    private static final String API_KEY = System.getenv().get("apiKey");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    @Transactional
    public ListOfRatesDto getRates(String codeOfBaseCurrency,
                                   @Nullable String codeOfCurrencies,
                                   @Nullable String typeOfCurrencies) {

        Call<ListOfRatesDto> request = clientApi.getAllRates(API_KEY, codeOfBaseCurrency, codeOfCurrencies, typeOfCurrencies);
        try {
            Response<ListOfRatesDto> result = request.execute();
            if (!result.isSuccessful() || result.body() == null) {  // StringUtils.isBlank(result.body()) ?
                throw new NotFoundException("Rates not found.");
            }
            ListOfRatesDto listOfRatesDto = result.body();

            LocalDate dateTime = LocalDate.parse(listOfRatesDto.meta().get("last_updated_at"), DATE_TIME_FORMATTER);
            Cur baseCurrency = currencyService.findCurrency(codeOfBaseCurrency);
            for (String key : listOfRatesDto.data().keySet()) {
                if (key.equals(codeOfBaseCurrency)){
                    continue;
                }
                Cur currencies = currencyService.findCurrency(listOfRatesDto.data().get(key).code());
                rateRepository.save(new Rate(
                        dateTime,
                        baseCurrency,
                        currencies,
                        listOfRatesDto.data().get(key).value()
                ));
            }
            return listOfRatesDto;
        } catch (IOException e) {
            throw new NotFoundException("IO ERROR:" + e.getMessage());
        }
    }

}
