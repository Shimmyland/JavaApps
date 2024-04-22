package org.example.exchangerates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.entity.Currency;
import org.example.exchangerates.entity.Rate;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final CurrencyService currencyService;
    private final RateRepository rateRepository;
    private final CurrencyApiClient currencyApiClient;
    private final CurrencyApiProperties currencyApiProperties;

    @Transactional
    public RatesDto setRates(final String codeOfBaseCurrency, final String codeOfCurrencies, final String typeOfCurrencies) {

        Call<RatesDto> request = currencyApiClient.getAllRates(currencyApiProperties.getAccessKey(), codeOfBaseCurrency, codeOfCurrencies, typeOfCurrencies);

        try {
            Response<RatesDto> result = request.execute();
            if (!result.isSuccessful() || result.body() == null) {
                throw new NotFoundException("Rates not found based on the request.");
            }
            RatesDto ratesDto = result.body();

            LocalDate dateTime = LocalDate.parse(ratesDto.meta().get("last_updated_at").substring(0,10));
            Currency baseCurrency = currencyService.findCurrencyBy(codeOfBaseCurrency.toUpperCase());

            for (String key : ratesDto.data().keySet()) {
                if (key.equals(codeOfBaseCurrency) || rateRepository.existsByParams(codeOfBaseCurrency, key, dateTime)){
                    continue;
                }
                Currency currencies = currencyService.findCurrencyBy(ratesDto.data().get(key).code());
                rateRepository.save(new Rate(
                        dateTime,
                        baseCurrency,
                        currencies,
                        ratesDto.data().get(key).value()
                ));
            }
            return ratesDto;
        } catch (IOException e) {
            throw new NotFoundException("IO ERROR:" + e.getMessage());
        }
    }

}
