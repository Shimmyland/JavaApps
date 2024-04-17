package org.example.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {

    private String code;
    private String name;
    @JsonProperty("name_plural")
    private String namePlural;
    private String symbol;
    @JsonProperty("symbol_native")
    private String symbolNative;
    private String type;
    @JsonProperty("decimal_digits")
    private int decimalDigits;
    private int rounding;
    @JsonProperty("icon_name")
    private String iconName;
    private List<String> countries;
}