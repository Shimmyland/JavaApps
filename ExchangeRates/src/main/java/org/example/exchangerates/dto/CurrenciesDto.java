package org.example.exchangerates.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.exchangerates.deserializer.CurrenciesDeserializer;
import java.util.HashMap;

@JsonDeserialize(using = CurrenciesDeserializer.class)
public record CurrenciesDto(HashMap<String, CurrencyDto> data) {
    public record CurrencyDto(String code, String name, String symbol, String type) {}
}
