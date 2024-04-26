package org.example.exchangerates.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.BothParamsCantBePresentException;
import org.example.exchangerates.service.RateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rates")
@Tag(name = "Rates")
public class RateController {

    private final RateService rateService;

    @Operation(
            description = "Based on your parameters it will request currencies from 3rd party API on previous day. You have three optional parameters. " +
                    "First is base currency, which is set to default as 'USD'. Other one is list of 'currencies' " +
                    "if you want to get only specific ones and the last one is 'type' for type of currencies. The only allowed type is 'fiat', 'metal' or 'crypto'. " +
                    "If you dont enter any value for 'type' or 'currencies' it will include everything available.",
            summary = "Responsible for getting rates from API and saving them into database.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RatesDto.class),
                                    examples = @ExampleObject(value = "{meta: base_currency: USD, from_date: 2024-04-25},"+
                                            "{data: {EUR: {code: EUR, value: 0.89765},GBP: {code: GBP, value: 0.77312}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful or if its body is empty.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Rates not found based on the request.'}"))
                    )
            }
    )


    // https://currencyapi.com/docs/latest
    @PostMapping("/latest")
    public ResponseEntity<RatesDto> setRates(@RequestParam(required = false, value = "base_currency", defaultValue = "USD") String baseCurrency,
                                             @RequestParam(required = false) String currencies,
                                             @RequestParam(required = false) String type) {
        if (StringUtils.isNotBlank(currencies) && StringUtils.isNotBlank(type)) {
            throw new BothParamsCantBePresentException("Parameter 'currencies' and 'type' cannot be present simultaneously.");
        }
        if (StringUtils.isNotBlank(currencies) && currencies.contains(baseCurrency)) {
            throw new DuplicateException("Base currency is included in the list of currencies.");
        }
        return ResponseEntity.ok(rateService.setRates(baseCurrency, currencies, type));
    }



    @Operation(
            description = "Based on your parameters it will request currencies from database. You have three optional parameters. " +
                    "First is base currency, which is set to default as 'USD'. Other one is 'type' for type of currencies which if is not" +
                    " set it includes everything. The only allowed type is 'fiat', 'metal' or 'crypto'. " +
                    "Last parameter is 'date' to specify the day of the exchange rates set to default to return the last day of saved exchange rates into database.",
            summary = "Responsible for listing rates from database.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RatesDto.class),
                                    examples = @ExampleObject(value = "{meta: from_date: 2024-04-25, base_currency: USD},"+
                                            "{data: {EUR: {code: EUR, value: 0.89765},GBP: {code: GBP, value: 0.77312}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful, if its body is empty, if in the request is 'currencies' " +
                                    "and 'type' present simultaneously or if the 'base_currency' is present in the list of 'currencies'.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Rates not found in database.'}"))
                    )
            }
    )
    @GetMapping("/history")
    public ResponseEntity<RatesDto> getRates(@RequestParam(required = false, value = "base_currency", defaultValue = "USD") String baseCurrency,
                                             @RequestParam(required = false) String type,
                                             @RequestParam(required = false) String date) {
        return ResponseEntity.ok(rateService.getRates(baseCurrency, type, date));
    }

}
