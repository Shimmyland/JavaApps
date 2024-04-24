package org.example.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.exception.InvalidInputException;
import org.example.exchangerates.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    // https://currencyapi.com/docs/currencies

    @GetMapping
    public ResponseEntity<CurrenciesDto> getCurrenciesByPage(@RequestParam int page){
        return ResponseEntity.ok(currencyService.getCurrenciesByPage(page));
    }

    @GetMapping("/all")
    public ResponseEntity<CurrenciesDto> getAllCurrencies(){
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrenciesDto> getCurrencyByCode(@PathVariable final String code){
        return ResponseEntity.ok(currencyService.getSpecificCurrency(code));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<CurrenciesDto> getCurrenciesByType(@PathVariable final String type){
        if (!type.equals("fiat") && !type.equals("crypto") && !type.equals("metal")){
            throw new InvalidInputException("Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.");
        }
        return ResponseEntity.ok(currencyService.getCurrenciesBy(type));
    }

}
