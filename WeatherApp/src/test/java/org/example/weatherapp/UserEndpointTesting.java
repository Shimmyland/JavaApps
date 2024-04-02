package org.example.weatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.weatherapp.models.DTOs.WeatherInputDTO;
import org.example.weatherapp.models.User;
import org.example.weatherapp.repositories.UserRepository;
import org.example.weatherapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserEndpointTesting {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper op;

    @BeforeEach
    void setUp() {
        op = new ObjectMapper();
        userRepository.deleteAll();
    }

    // registration
    @Test
    public void successfulTestingRegistrationEndpoint() throws Exception {
        User user = new User(
                "pepik10",
                "Password1!",
                "skokan.josef@gmail.com",
                "Josef",
                "Skokan",
                true,
                "Token"
        );
        userRepository.save(user);

        WeatherInputDTO weatherInputDTO = new WeatherInputDTO(user.getId(), "paris");
        mockMvc.perform(post("/weather")
                        .content(op.writeValueAsString(weatherInputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
