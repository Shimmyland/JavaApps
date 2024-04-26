package org.example.exchangerates.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.dto.RatesDto;
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
            description = "Based on your entered number of 'page', it will return a list of currencies. Page size is set to list of ten currencies and the default value is set to 0.",
            summary = "Responsible for listing currencies from database by page.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrenciesDto.class),
                                    examples = @ExampleObject(value = "{data: {EUR: {code: EUR, name: Euro, symbol: €, type: fiat},GBP: {code: GBP, name: British Pound Sterling, symbol: £, type: fiat}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful, if its body is empty.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Currency not found.'}"))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<CurrenciesDto> getCurrenciesByPage(@RequestParam (defaultValue = "0") int page){
        return ResponseEntity.ok(currencyService.getCurrenciesByPage(page));
    }

    @Operation(
            summary = "Responsible for listing all currencies saved in database.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrenciesDto.class),
                                    examples = @ExampleObject(value = "{data: {EUR: {code: EUR, name: Euro, symbol: €, type: fiat},GBP: {code: GBP, name: British Pound Sterling, symbol: £, type: fiat}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful, if its body is empty.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Currency not found.'}"))
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<CurrenciesDto> getAllCurrencies(){
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @Operation(
            summary = "Responsible for listing specific currency saved in database.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrenciesDto.class),
                                    examples = @ExampleObject(value = "{data: {EUR: {code: EUR, name: Euro, symbol: €, type: fiat}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful, if its body is empty.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Currency not found.'}"))
                    )
            }
    )
    @GetMapping("/{code}")
    public ResponseEntity<CurrenciesDto> getCurrencyByCode(@PathVariable final String code){
        return ResponseEntity.ok(currencyService.getSpecificCurrency(code));
    }

    @Operation(
            summary = "Responsible for listing currencies by type from database.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the request was successful.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrenciesDto.class),
                                    examples = @ExampleObject(value = "{data: {EUR: {code: EUR, name: Euro, symbol: €, type: fiat},GBP: {code: GBP, name: British Pound Sterling, symbol: £, type: fiat}}}"))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Returned when request was not successful, if its body is empty.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{'error' : 'Currency not found.'}"))
                    )
            }
    )
    @GetMapping("/type/{type}")
    public ResponseEntity<CurrenciesDto> getCurrenciesByType(@PathVariable final String type){
        if (!type.equals("fiat") && !type.equals("crypto") && !type.equals("metal")){
            throw new InvalidInputException("Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.");
        }
        return ResponseEntity.ok(currencyService.getCurrenciesBy(type));
    }

}
