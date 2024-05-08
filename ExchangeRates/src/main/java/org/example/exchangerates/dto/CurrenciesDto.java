package org.example.exchangerates.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.exchangerates.deserializer.CurrenciesDeserializer;
import java.util.LinkedHashMap;

@Schema(description = "Object for list of currencies")
@JsonDeserialize(using = CurrenciesDeserializer.class)
public record CurrenciesDto(@Schema(description = "Map containing currency code as key and CurrencyDto as value") LinkedHashMap<String, CurrencyDto> data) {

    public record CurrencyDto(@Schema(description = "Code of the currency", example = "CZK") String code,
                              @Schema(description = "Name of the currency",example = "Czech Republic Koruna") String name,
                              @Schema(description = "Symbol of the currency", example = "Kč") String symbol,
                              @Schema(description = "Type of the currency",example = "fiat") String type) {

    }
}
