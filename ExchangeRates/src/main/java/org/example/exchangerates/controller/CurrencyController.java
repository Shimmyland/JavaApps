package org.example.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.ListOfCurrenciesDto;
import org.example.exchangerates.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("") // https://currencyapi.com/docs/currencies
    public ResponseEntity<ListOfCurrenciesDto> getAllCurrencies(){
        return ResponseEntity.ok(currencyService.createNewCurrencies());
    }

}
