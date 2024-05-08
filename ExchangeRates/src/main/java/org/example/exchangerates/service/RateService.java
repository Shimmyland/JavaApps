package org.example.exchangerates.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.entity.Rate;
import org.example.exchangerates.exception.InvalidInputException;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final CurrencyService currencyService;
    private final RateRepository rateRepository;
    private final CurrencyApiClient currencyApiClient;
    private final CurrencyApiProperties currencyApiProperties;

    @Transactional
    public RatesDto getRates(final String baseCurrency, final String currencies, final String type, LocalDate date) {
        date = Objects.requireNonNullElseGet(date, () -> LocalDate.now().minusDays(1));

        List<Rate> tmp = ratesFromDatabase(baseCurrency, currencies, type, date);
        if (!tmp.isEmpty()) {
            return modelData(tmp, date, baseCurrency);
        }
        return fetchAndSaveData(baseCurrency, currencies, type, date);
    }

    List<Rate> ratesFromDatabase(final String baseCurrency, final String currencies, final String type, final LocalDate date) {
        List<Rate> tmp = List.of();

        if (StringUtils.isNotBlank(currencies)) {
            List<String> listOfCurrencies = Arrays.asList(currencies.toUpperCase().split(","));
            if (listOfCurrencies.size() != currencyService.countCurrencies(listOfCurrencies)) {
                throw new InvalidInputException("List of currencies contains some invalid data.");
            }
            tmp = rateRepository.findAllByBaseCurrency_CodeAndCurrency_CodeInAndFromDate(baseCurrency, listOfCurrencies, date);
            if (tmp.size() != listOfCurrencies.size()) {
                return Collections.emptyList();
            }
        }

        if (StringUtils.isNotBlank(type)) {
            tmp = rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(baseCurrency, type, date);
            int number = currencyService.findCurrencyBy(baseCurrency).getType().equals(type) ? 1 : 0;
            if (tmp.size() != currencyService.countAllCurrenciesByType(type) - number) {
                return Collections.emptyList();
            }
        }

        if (!StringUtils.isNotBlank(currencies) && !StringUtils.isNotBlank(type)){
            tmp = rateRepository.findAllByBaseCurrency_CodeAndFromDate(baseCurrency, date);
            if (tmp.size() != currencyService.countAllCurrencies() - 1) {   // -1 -> minus base Currency
                return Collections.emptyList();
            }
        }

        return tmp;
    }

    RatesDto modelData(final List<Rate> tmp, final LocalDate localDate, final String baseCurrency) {
        HashMap<String, String> meta = new HashMap<>();
        meta.put("from_date", localDate.toString());
        meta.put("base_currency", baseCurrency);
        HashMap<String, RatesDto.RateDto> data = new HashMap<>();
        for (Rate rate : tmp) {
            RatesDto.RateDto rateDto = new RatesDto.RateDto(rate.getCurrency().getCode(), rate.getPrice());
            data.put(rate.getCurrency().getCode(), rateDto);
        }
        return new RatesDto(meta, data);
    }

    RatesDto fetchAndSaveData(final String baseCurrency, final String currencies, final String type, final LocalDate date) {
        Call<RatesDto> request = currencyApiClient.getAllRates(currencyApiProperties.getAccessKey(), date, baseCurrency, currencies, type);
        try {
            Response<RatesDto> result = request.execute();
            if (!result.isSuccessful() || result.body() == null) {
                throw new NotFoundException("No rates found.");
            }
            RatesDto ratesDto = result.body();

            int count = ratesDto.data().size();
            for (String key : ratesDto.data().keySet()) {
                if (key.equals(baseCurrency) || rateRepository.existsByParams(baseCurrency, key, date)) {
                    count--;
                    continue;
                }
                rateRepository.save(Rate.builder()
                        .fromDate(date)
                        .savedAt(LocalDateTime.now())
                        .baseCurrency(currencyService.findCurrencyBy(baseCurrency.toUpperCase()))
                        .currency(currencyService.findCurrencyBy(ratesDto.data().get(key).code()))
                        .price(ratesDto.data().get(key).value())
                        .build());
            }
            log.info("New {} rates saved into database.", count);
            return ratesDto;
        } catch (IOException e) {
            throw new NotFoundException("IO ERROR:" + e.getMessage());
        }
    }
}
