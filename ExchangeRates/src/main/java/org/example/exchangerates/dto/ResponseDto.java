package org.example.exchangerates.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Object for errors")
public record ResponseDto(@Schema(example = "Currency not found.") String message) {
}
