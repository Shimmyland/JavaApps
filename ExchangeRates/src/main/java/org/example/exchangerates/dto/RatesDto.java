package org.example.exchangerates.dto;

import java.util.HashMap;

public record RatesDto(HashMap<String, String> meta, HashMap<String, RateDto> data) {
    public record RateDto(String code, double value) {}
}