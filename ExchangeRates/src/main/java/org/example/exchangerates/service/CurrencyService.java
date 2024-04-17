package org.example.exchangerates.service;

import lombok.RequiredArgsConstructor;
import org.example.exchangerates.client.ClientApi;
import org.example.exchangerates.dto.CurrencyDto;
import org.example.exchangerates.dto.ListOfCurrenciesDto;
import org.example.exchangerates.entity.Cur;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ClientApi clientApi;
    private static final String API_KEY = System.getenv().get("apiKey");

    protected Cur findCurrency(String codeOfCurrency){
        Optional<Cur> tmp = currencyRepository.findByCode(codeOfCurrency);
        return tmp.orElseThrow(() -> new NotFoundException("Currency not found"));
    }
    @Transactional
    public ListOfCurrenciesDto createNewCurrencies() {
        Call<ListOfCurrenciesDto> request = clientApi.getAllCurrencies(API_KEY);

        try {
            Response<ListOfCurrenciesDto> response = request.execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new NotFoundException("Currency not found.");
            }
            ListOfCurrenciesDto currencies = response.body();

            Set<String> existingCodes = currencyRepository.findAllCodes();
            for (String key : currencies.data().keySet()) {
                if (existingCodes.contains(key)){
                    continue;
                }
                CurrencyDto tmp = currencies.data().get(key);
                currencyRepository.save(new Cur(
                        tmp.getCode(),
                        tmp.getName(),
                        tmp.getSymbol(),
                        tmp.getType()
                ));
            }
            return currencies;

        } catch (IOException e) {
            throw new NotFoundException("Something with IO." + e.getMessage());
        }
    }
}
