package org.example.exchangerates.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.BothParamsCantBePresentException;
import org.example.exchangerates.exception.InvalidInputException;
import org.example.exchangerates.service.RateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rates")
@Tag(name = "Rates")
public class RateController {

    private final RateService rateService;

// https://currencyapi.com/docs/historical

    @Operation(description = "This endpoint manages currency exchange rates. It retrieves rates based on specified parameters. " +
            "If the rates are not stored locally, it fetches them from a third-party API for the previous day's data. " +
            "The endpoint accepts four optional parameters: 'base_currency', defaulted to 'USD'; 'currencies', a list of specific currencies (in uppercase) desired; " +
            "'type', specifying currency type ('fiat', 'metal', or 'crypto'); and 'date', defaulted to yesterday's date. " +
            "If 'type' or 'currencies' are not provided, rates for all available currencies are included. " +
            "It's not permitted to provide both 'currencies' and 'type' simultaneously.",
            summary = "Retrieves currency exchange rates from the database or API.")
    @ApiResponse(responseCode = "200", ref = "getRates200")
    @ApiResponse(responseCode = "400", ref = "getRates400")
    @ApiResponse(responseCode = "404", ref = "getRates404")
    @GetMapping
    public ResponseEntity<RatesDto> getRates(@RequestParam(required = false, value = "base_currency", defaultValue = "USD") String baseCurrency,
                                             @RequestParam(required = false) String currencies,
                                             @RequestParam(required = false) String type,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (StringUtils.isNotBlank(currencies) && StringUtils.isNotBlank(type)) {
            throw new BothParamsCantBePresentException("Parameter 'currencies' and 'type' cannot be present simultaneously.");
        }
        if (StringUtils.isNotBlank(currencies) && currencies.contains(baseCurrency)) {
            throw new DuplicateException("Base currency is included in the list of currencies.");
        }
        if (StringUtils.isNotBlank(type) && !(type.equals("fiat") || type.equals("metal") || type.equals("crypto"))) {
            throw new InvalidInputException("Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.");
        }
        return ResponseEntity.ok(rateService.getRates(baseCurrency.toUpperCase(), currencies, type, date));
    }
}
