package org.example.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.ListOfRatesDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.ParamsCantBePresentException;
import org.example.exchangerates.service.RateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("latest")
public class RateController {

    /*
    https://currencyapi.com/docs/latest
    */

    private final RateService rateService;

    @PostMapping("") // http://localhost:8080/latest?base_currency=CZK&currencies=EUR,USD // @RequestParam List<String> currencies
    public ResponseEntity<ListOfRatesDto> getAllRates(@RequestParam (value = "base_currency", defaultValue = "USD") String baseCurrency,
                                                      @RequestParam (required = false) String currencies,
                                                      @RequestParam (required = false) String type){
        if (currencies != null && type != null) {
            throw new ParamsCantBePresentException("Parameter 'currencies' and 'type' cannot be present simultaneously.");
        }
        if (currencies != null && currencies.contains(baseCurrency)){
            throw new DuplicateException("Base currency is included in the list of currencies.");
        }
        return ResponseEntity.ok(rateService.getRates(baseCurrency, currencies, type));
    }

    // 3 optional params - baseCurrency (default USD), currencies(default not set), type (default ALL)
    // currencies and type cannot be together - return empty list
    // endpoint combination
    // - baseCurrency (all Rates)
    // - baseCurrency + currencies
    // - baseCurrency + type
    // - currencies (in USD)
    // - type (in USD)
}
