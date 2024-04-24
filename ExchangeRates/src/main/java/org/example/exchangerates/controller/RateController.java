package org.example.exchangerates.controller;

import io.micrometer.common.util.StringUtils;
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
public class RateController {

    private final RateService rateService;

    @PostMapping("/latest") // https://currencyapi.com/docs/latest
    public ResponseEntity<RatesDto> setRates(@RequestParam (required = false, value = "base_currency", defaultValue = "USD") String baseCurrency,
                                                @RequestParam (required = false) String currencies,
                                                @RequestParam (required = false) String type){
        if (StringUtils.isNotBlank(currencies) && StringUtils.isNotBlank(type)) {
            throw new BothParamsCantBePresentException("Parameter 'currencies' and 'type' cannot be present simultaneously.");
        }
        if (StringUtils.isNotBlank(currencies) && currencies.contains(baseCurrency)){
            throw new DuplicateException("Base currency is included in the list of currencies.");
        }
        return ResponseEntity.ok(rateService.setRates(baseCurrency, currencies, type));
    }

    @GetMapping("/history")
    public ResponseEntity<RatesDto> getRates(@RequestParam(required = false, value = "base_currency", defaultValue = "USD") String baseCurrency,
                                             @RequestParam(required = false) String type,
                                             @RequestParam(required = false) String date){
        return ResponseEntity.ok(rateService.getRates(baseCurrency, type, date));
    }

}
