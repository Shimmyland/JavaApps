package org.example.weatherapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.weatherapp.dto.InputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WeatherControllerTest {

    // integration - use only for controller
    @Autowired
    MockMvc mockMvc;

    private ObjectMapper op;

    @BeforeEach
    void setUp(){
        op = new ObjectMapper();
    }

    @Test
    void searchForWeather_successful() throws Exception {
        InputDto inputDto = new InputDto("london");

        mockMvc.perform(post("/weathers/search")
                .content(op.writeValueAsString(inputDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void listWeatherFromDatabase_successful() throws Exception {
        InputDto inputDto = new InputDto("london");

        mockMvc.perform(get("/weathers/search")
                        .content(op.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


//    @Test
//    void testListWeatherFromDatabase() throws Exception {
//        // Arrange - Mocking data
//        WeatherListDto mockWeatherList = new WeatherListDto();
//        WeatherListDto.WeatherDto mockWeather = new WeatherListDto.WeatherDto(
//                LocalDateTime.now(),"city1", "country1", 20.5,  "cloudy");
//        mockWeatherList.getResult().add(mockWeather);
//
//        List<Weather> mockList = new ArrayList<>();
//        mockList.add(new Weather(
//                "city1",
//                "country1",
//                20.5,
//                "cloudy"
//        ));
//
//        // Act - Mocking service method
//        Mockito.when(weatherRepository.findTop100ByCityContainingOrderByCreateAt("")).thenReturn((mockList));
//        Mockito.when(weatherService.getWeatherBy("")).thenReturn(mockWeatherList);
//
//
//        // Assert - Perform GET request and assert response
//        mockMvc.perform(get("/weathers/search")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result[0].city").value("city1"))
//                .andExpect(jsonPath("$.weatherList[0].country").value("country1"))
//                .andExpect(jsonPath("$.weatherList[0].temperature").value(20.5))
//                .andExpect(jsonPath("$.weatherList[0].weather").value("cloudy"));
//    }
}
