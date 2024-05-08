package org.example.exchangerates.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    // https://currencyapi.com/docs/currencies

    @Operation(
            description = "Based on your entered number of 'page', it will return a list of 10 currencies." +
            "If you place a number which is higher then the total number of pages, it will return an empty list of 'data'.",
            summary = "Responsible for listing currencies from database by page.")
    @ApiResponse(responseCode = "200", ref = "getCurrenciesByPage200")
    @ApiResponse(responseCode = "400", ref = "getCurrenciesByPage400")
    @GetMapping
    public ResponseEntity<CurrenciesDto> getCurrenciesByPage(@RequestParam(defaultValue = "0") int page) {
        if (page < 0){
            throw new InvalidInputException("Invalid input, the page cannot be negative number.");
        }
        return ResponseEntity.ok(currencyService.getCurrenciesByPage(page));
    }

    @Operation(summary = "Responsible for listing all currencies saved in database.")
    @ApiResponse(responseCode = "200", ref = "getAllCurrencies200")
    @GetMapping("/all")
    public ResponseEntity<CurrenciesDto> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @Operation(summary = "Responsible for listing specific currency saved in database.")
    @ApiResponse(responseCode = "200", ref = "getCurrencyByCode200")
    @ApiResponse(responseCode = "400", ref = "getCurrencyByCode400")
    @GetMapping("/{code}")
    public ResponseEntity<CurrenciesDto> getCurrencyByCode(@PathVariable final String code) {
        return ResponseEntity.ok(currencyService.getSpecificCurrency(code));
    }

    @Operation(summary = "Responsible for listing currencies by type from database.")
    @ApiResponse(responseCode = "200", ref = "getCurrenciesByType200")
    @ApiResponse(responseCode = "400", ref = "getCurrenciesByType400")
    @GetMapping("/type/{type}")
    public ResponseEntity<CurrenciesDto> getCurrenciesByType(@PathVariable final String type) {
        if (!type.equals("fiat") && !type.equals("crypto") && !type.equals("metal")) {
            throw new InvalidInputException("Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.");
        }
        return ResponseEntity.ok(currencyService.getCurrenciesBy(type));
    }

}
