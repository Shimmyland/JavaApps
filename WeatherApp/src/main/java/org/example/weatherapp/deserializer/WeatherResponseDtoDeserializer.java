package org.example.weatherapp.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.weatherapp.dto.WeatherResponseDto;
import java.io.IOException;

public class WeatherResponseDtoDeserializer extends StdDeserializer<WeatherResponseDto> {

    public WeatherResponseDtoDeserializer(){
        super(WeatherResponseDto.class);
    }

    @Override
    public WeatherResponseDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        return new WeatherResponseDto(
                node.get("name").asText(),
                node.get("sys").get("country").asText(),
                node.get("weather").get(0).get("main").asText(),
                node.get("main").get("temp").asDouble());
    }
}
