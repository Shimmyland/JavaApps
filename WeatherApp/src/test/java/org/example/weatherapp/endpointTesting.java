package org.example.weatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.weatherapp.models.DTOs.InputDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class endpointTesting {

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

    @Test
    public void testingEndpointSuccess() throws Exception {
        User user = new User(
                "Shimmyland",
                "Password1!",
                "simonlibiger@seznam.com",
                "Simon",
                "Libiger",
                false,
                "Token"
        );
        user = userRepository.save(user);

        InputDTO inputDTO = new InputDTO(user.getId(), "paris");
        mockMvc.perform(post("/api/weather")
                .content(op.writeValueAsString(inputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
