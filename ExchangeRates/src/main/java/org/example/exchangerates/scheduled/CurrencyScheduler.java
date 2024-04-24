package org.example.exchangerates.scheduled;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.service.CurrencyService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyScheduler {

    private final CurrencyService currencyService;

    @PostConstruct          // "used on a method that needs to be executed after dependency injection is done to perform any initialization"
    public void init() {    // change it and use commandLineRunner
        try {
            log.info("INIT: Currencies added into the DB: {}, request was successful.", currencyService.setNewCurrencies());
        } catch (Exception e){
            log.error("THERE IS AN ISSUE WITH SAVING CURRENCIES: " + e.getMessage());
        }
    }

}
