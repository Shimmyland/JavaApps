package org.example.exchangerates.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        // rates - endpoints
        ApiResponse getRates200 = new ApiResponse().description("OK").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("""
                        {
                          "meta": {
                            "last_updated_at": "2024-05-09T23:59:59Z"
                          },
                          "data": {
                            "EUR": {
                              "code": "EUR",
                              "value": 0.9272301721
                            },
                            "GBP": {
                              "code": "GBP",
                              "value": 0.7985101343
                            },
                            "...": "170+ more currencies"
                          }
                        }
                        """)));
        ApiResponse getRates400 = new ApiResponse().description("Bad Request").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType()
                        .addExamples("'currencies' and 'type' param present simultaneously", new Example().value("{\"message\":\"Parameter 'currencies' and 'type' cannot be present simultaneously.\"}"))
                        .addExamples("'base_currency' included in 'currencies'", new Example().value("{\"message\":\"Base currency is included in the list of currencies.\"}"))
                        .addExamples("invalid input for 'type'", new Example().value("{\"message\":\"Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.\"}"))
                        .addExamples("invalid input for 'date'", new Example().value("{\"message\":\"Unable to convert String input 'date' into LocalDate.\"}"))
                        .addExamples("invalid input for 'currencies'", new Example().value("{\"message\":\"List of currencies contains some invalid data.\"}"))));
        ApiResponse getRates404 = new ApiResponse().description("Not Found").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("{\"message\":\"Rates not found based on the request.\"}")));


        // currencies - endpoints
        ApiResponse getCurrenciesByPage200 = new ApiResponse().description("OK").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("""
                        {
                          "data": {
                            "AED": {
                              "code": "AED",
                              "name": "United Arab Emirates Dirham",
                              "symbol": "AED",
                              "type": "fiat"
                            },
                            "AUD": {
                              "code": "AUD",
                              "name": "Australian Dollar",
                              "symbol": "AU$",
                              "type": "fiat"
                            },
                            "ALL": {
                              "code": "ALL",
                              "name": "Albanian Lek",
                              "symbol": "ALL",
                              "type": "fiat"
                            },
                            "...": "7 more currencies"
                          }
                        }
                        """)));
        ApiResponse getCurrenciesByPage400 = new ApiResponse().description("Returned when you place a negative number.").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("{\"message\":\"Invalid input, the page cannot be negative number.\"}")));

        ApiResponse getAllCurrencies200 = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("""
                        {
                          "data": {
                            "AED": {
                              "code": "AED",
                              "name": "United Arab Emirates Dirham",
                              "symbol": "AED",
                              "type": "fiat"
                            },
                            "AFN": {
                              "code": "AFN",
                              "name": "Afghan Afghani",
                              "symbol": "Af",
                              "type": "fiat"
                            },
                            "...": "170+ more currencies"
                          }
                        }
                        """)));

        ApiResponse getCurrencyByCode200 = new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("""
                        {
                            "data": {
                                "CZK": {
                                    "code": "CZK",
                                    "name": "Czech Republic Koruna",
                                    "symbol": "Kč",
                                    "type": "fiat"
                                }
                            }
                        }
                        """)));
        ApiResponse getCurrencyByCode400 = new ApiResponse().description("When currency is not found.")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().example("{\"message\":\"Currency not found.\"}")));

        ApiResponse getCurrenciesByType200 = new ApiResponse().description("OK").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("""
                        {
                          "data": {
                            "XAG": {
                              "code": "XAG",
                              "name": "Silver Ounce",
                              "symbol": "XAG",
                              "type": "metal"
                            },
                            "XAU": {
                              "code": "XAU",
                              "name": "Gold Ounce",
                              "symbol": "XAU",
                              "type": "metal"
                            },
                            "XPD": {
                              "code": "XPD",
                              "name": "Palladium Ounce",
                              "symbol": "XPD",
                              "type": "metal"
                            },
                            "XPT": {
                              "code": "XPT",
                              "name": "Platinum Ounce",
                              "symbol": "XPT",
                              "type": "metal"
                            }
                          }
                        }
                        """)));
        ApiResponse getCurrenciesByType400 = new ApiResponse().description("If you enter something else.").content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType().example("{\"message\":\"Invalid input type. It has to be 'fiat', 'crypto' or 'metal'.\"}")));

        Components components = new Components();
        components.addResponses("getRates200", getRates200);
        components.addResponses("getRates400", getRates400);
        components.addResponses("getRates404", getRates404);
        components.addResponses("getCurrenciesByPage200", getCurrenciesByPage200);
        components.addResponses("getCurrenciesByPage400", getCurrenciesByPage400);
        components.addResponses("getAllCurrencies200", getAllCurrencies200);
        components.addResponses("getCurrencyByCode200", getCurrencyByCode200);
        components.addResponses("getCurrencyByCode400", getCurrencyByCode400);
        components.addResponses("getCurrenciesByType200", getCurrenciesByType200);
        components.addResponses("getCurrenciesByType400", getCurrenciesByType400);

        return new OpenAPI()
                .components(components)
                .info(new Info().title("E.R. Doc").version("1.0").description("Documentation for my ExchangeRates application."));
    }
}
