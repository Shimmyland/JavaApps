package org.example.exchangerates.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;

@Schema(description = "Object for list of rates")
public record RatesDto(@Schema(description = "Meta information about the rates") HashMap<String, String> meta,
                       @Schema(description = "Map containing currency code as key and RateDto as value") HashMap<String, RateDto> data) {

    public record RateDto(@Schema(description = "Currency code", example = "CZK") String code,
                          @Schema(description = "Rate value", example = "2.3") double value) {
    }
}