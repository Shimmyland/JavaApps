package org.example.exchangerates.dto;

import java.util.HashMap;

public record ListOfCurrenciesDto(HashMap<String, CurrencyDto> data) {
}
