package org.example.exchangerates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.entity.Currency;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyApiClient currencyApiClient;
    private final CurrencyApiProperties currencyApiProperties;

    Currency findCurrencyBy(final String code){
       return currencyRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Currency not found."));
    }

    @Transactional
    public int setNewCurrencies() {
        Call<CurrenciesDto> request = currencyApiClient.getAllCurrencies(currencyApiProperties.getAccessKey());

        try {
            Response<CurrenciesDto> response = request.execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new NotFoundException("Currency not found.");
            }
            CurrenciesDto currencies = response.body();

            Set<String> existingCodes = currencyRepository.findAllCodes();
            int count = currencies.data().size();
            for (String key : currencies.data().keySet()) {
                if (existingCodes.contains(key)){
                    count--;
                    continue;
                }
                CurrenciesDto.CurrencyDto tmp = currencies.data().get(key);
                currencyRepository.save(new Currency(
                        tmp.code(),
                        tmp.name(),
                        tmp.symbol(),
                        tmp.type()
                ));
            }
            return count;
        } catch (IOException e) {
            throw new NotFoundException("Something with IO." + e.getMessage());
        }
    }

    CurrenciesDto modelData(final List<Currency> currencies){
        HashMap<String, CurrenciesDto.CurrencyDto> currenciesMap = new HashMap<>();
        for (Currency currency : currencies){
            CurrenciesDto.CurrencyDto currencyDto = new CurrenciesDto.CurrencyDto(
                    currency.getCode(),
                    currency.getName(),
                    currency.getSymbol(),
                    currency.getType()
            );
            currenciesMap.put(currency.getCode(), currencyDto);
        }
        return new CurrenciesDto(currenciesMap);
    }


    @Transactional(readOnly = true)
    public CurrenciesDto getAllCurrencies(){
        List<Currency> currencies = currencyRepository.findAllCurrencies();
        return modelData(currencies);
    }

    @Transactional(readOnly = true)
    public CurrenciesDto getSpecificCurrency(final String code){
        List<Currency> currencies = new ArrayList<>();
        currencies.add(findCurrencyBy(code));
        return modelData(currencies);
    }

    @Transactional(readOnly = true)
    public CurrenciesDto getCurrenciesBy(final String type){
        List<Currency> currencies = currencyRepository.findAllByType(type);
        return modelData(currencies);
    }

}
