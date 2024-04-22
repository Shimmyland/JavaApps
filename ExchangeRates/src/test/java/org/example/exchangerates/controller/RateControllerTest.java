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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void setRates_successful() throws Exception {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("base_currency", "USD");
        requestParams.put("currencies", "EUR,GBP");
        requestParams.put("type", "fiat");

        mockMvc.perform(post("/latest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(op.writeValueAsString(requestParams)))
                .andExpect(status().isOk());
    }
}
