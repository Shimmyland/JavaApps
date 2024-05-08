package org.example.exchangerates.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.exchangerates.dto.CurrenciesDto;
import java.io.IOException;
import java.util.LinkedHashMap;

public class CurrenciesDeserializer extends StdDeserializer<CurrenciesDto> {

    public CurrenciesDeserializer() {
        super(CurrenciesDto.class);
    }

    @Override
    public CurrenciesDto deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        LinkedHashMap<String, CurrenciesDto.CurrencyDto> data = new LinkedHashMap<>();

        node.get("data").fields().forEachRemaining(entry -> {
            CurrenciesDto.CurrencyDto currencyDto = new CurrenciesDto.CurrencyDto(
                    entry.getValue().get("code").asText(),
                    entry.getValue().get("name").asText(),
                    entry.getValue().get("symbol").asText(),
                    entry.getValue().get("type").asText()
            );
            data.put(entry.getKey(), currencyDto);
        });

        return new CurrenciesDto(data);
    }
}