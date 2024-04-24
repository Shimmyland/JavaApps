package org.example.exchangerates.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.entity.Currency;
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
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

    private final CurrencyService currencyService;
    private final RateRepository rateRepository;
    private final CurrencyApiClient currencyApiClient;
    private final CurrencyApiProperties currencyApiProperties;

    @Transactional
    public RatesDto setRates(final String baseCurrency, final String currencies, final String type) {
        Call<RatesDto> request = currencyApiClient.getAllRates(currencyApiProperties.getAccessKey(), baseCurrency, currencies, type);
        try {
            Response<RatesDto> result = request.execute();
            if (!result.isSuccessful() || result.body() == null) {
                throw new NotFoundException("Rates not found based on the request.");
            }
            RatesDto ratesDto = result.body();

            LocalDate dateTime = LocalDate.parse(ratesDto.meta().get("last_updated_at").substring(0,10));
            Currency currencyTmp = currencyService.findCurrencyBy(baseCurrency.toUpperCase());

            for (String key : ratesDto.data().keySet()) {
                if (key.equals(baseCurrency) || rateRepository.existsByParams(baseCurrency, key, dateTime)){
                    continue;
                }
                rateRepository.save(new Rate(
                        dateTime,
                        currencyTmp,
                        currencyService.findCurrencyBy(ratesDto.data().get(key).code()),
                        ratesDto.data().get(key).value()
                ));
            }
            return ratesDto;
        } catch (IOException e) {
            throw new NotFoundException("IO ERROR:" + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public RatesDto getRates(final String baseCurrency, final String type, final String date){
        LocalDate localDate = StringUtils.isBlank(date) ? rateRepository.findByFromDate() : LocalDate.parse(date);
        List<Rate> tmp = StringUtils.isNotBlank(type) ?
                rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(baseCurrency, type, localDate) :
                rateRepository.findAllByBaseCurrency_CodeAndFromDate(baseCurrency, localDate);
        if (tmp.isEmpty()){
            throw new NotFoundException("No rates found.");
        }

        HashMap<String, String> meta = new HashMap<>();
        meta.put("from_date", localDate.toString());
        meta.put("base_currency", baseCurrency);
        HashMap<String, RatesDto.RateDto> data = new HashMap<>();
        for (Rate rate : tmp){
            RatesDto.RateDto rateDto = new RatesDto.RateDto(rate.getCurrency().getCode(), rate.getPrice());
            data.put(rate.getCurrency().getCode(), rateDto);
        }
        return new RatesDto(meta, data);
    }
}
