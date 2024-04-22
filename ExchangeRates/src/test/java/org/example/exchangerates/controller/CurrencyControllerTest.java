package org.example.exchangerates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
class CurrencyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllCurrencies_successful() throws Exception {
        mockMvc.perform(get("/currencies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
        // Add more assertions as needed
    }

    @Test
    void getCurrencyByCode_successful() throws Exception {
        String code = "USD";
        mockMvc.perform(get("/currencies/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void getCurrenciesByType_successful() throws Exception {
        String type = "fiat";
        mockMvc.perform(get("/currencies/type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }
}
