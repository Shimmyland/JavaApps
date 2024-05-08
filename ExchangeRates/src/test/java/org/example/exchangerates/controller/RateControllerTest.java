package org.example.exchangerates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
class RateControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper op;

    @BeforeEach
    void setUp(){
        op = new ObjectMapper();
    }

    @Test
    void getRates_successful() throws Exception {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("base_currency", "USD");
        requestParams.put("currencies", "EUR,GBP");
        requestParams.put("date", "2024-4-10");

        mockMvc.perform(get("/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(op.writeValueAsString(requestParams)))
                .andExpect(status().isOk());
    }

    @Test
    void getRates_bothParamsPresent() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("base_currency", "USD")
                        .param("currencies", "EUR,GBP")
                        .param("type", "fiat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parameter 'currencies' and 'type' cannot be present simultaneously."));
    }

    @Test
    void getRates_baseCurrencyInCurrencies() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("base_currency", "USD")
                        .param("currencies", "USD,EUR,GBP")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Base currency is included in the list of currencies."));
    }

    @Test
    void getRates_invalidTypeInput() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("base_currency", "USD")
                        .param("type", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input type. It has to be 'fiat', 'crypto' or 'metal'."));
    }
}
