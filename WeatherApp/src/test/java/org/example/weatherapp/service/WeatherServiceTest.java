package org.example.weatherapp.service;

import okhttp3.ResponseBody;
import org.example.weatherapp.client.WeatherAPI;
import org.example.weatherapp.dto.WeatherListDto;
import org.example.weatherapp.dto.WeatherResponseDto;
import org.example.weatherapp.entity.Weather;
import org.example.weatherapp.exception.WeatherNotFoundException;
import org.example.weatherapp.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    WeatherRepository weatherRepository;

    @Mock
    WeatherAPI weatherAPI;

    @InjectMocks
    WeatherService weatherService;


    @Test
    void createNewWeatherForecast_success() throws IOException {
        final var request = mock(Call.class);
        final WeatherResponseDto mockedResponse = new WeatherResponseDto(
                "city",
                "country",
                "clouds",
                200.0
        );
        final var mockedEntity = new Weather();

        when(weatherAPI.weatherRequest(anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(mockedResponse));
        when(weatherRepository.save(any(Weather.class))).thenReturn(mockedEntity);

        WeatherResponseDto response = weatherService.createNewWeatherForecast("");

        assertEquals(mockedResponse, response);
    }

    @Test
    void testCreateNewWeatherForecast_WeatherNotFound_404() throws IOException {
        // Arrange
        String city = "NonExistentCity";
        var request = Mockito.mock(Call.class);
        when(weatherAPI.weatherRequest(anyString(), anyString())).thenReturn(request);
        Response<WeatherResponseDto> response = Response.error(404, mock(ResponseBody.class));
        when(request.execute()).thenReturn(response);

        // Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.createNewWeatherForecast(city));
    }

    @Test
    void testCreateNewWeatherForecast_WeatherNotFound_Null() throws IOException {
        // Arrange
        String city = "NonExistentCity";
        var request = Mockito.mock(Call.class);

        // Act
        when(weatherAPI.weatherRequest(anyString(), anyString())).thenReturn(request);
        Response<WeatherResponseDto> response = Response.success(null);
        when(request.execute()).thenReturn(response);

        // Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.createNewWeatherForecast(city));
    }

    @Test
    void testCreateNewWeatherForecast_IOException() throws IOException {
        // Arrange
        String city = "NonExistentCity";
        var request = Mockito.mock(Call.class);

        // Act
        when(weatherAPI.weatherRequest(anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenThrow(new IOException());

        // Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.createNewWeatherForecast(city));
    }


    @Test
    void testListWeatherFromDatabase_successful() {
        // Arrange - Mocking data
        WeatherListDto mockedResult = new WeatherListDto();
        WeatherListDto.WeatherDto mockedWeather = new WeatherListDto.WeatherDto(
                LocalDateTime.now(),"city1", "country1", 20.5,  "cloudy");
        mockedResult.getResult().add(mockedWeather);

        List<Weather> mockedListFromDb = new ArrayList<>();
        mockedListFromDb.add(new Weather(
                "city1",
                "country1",
                20.5,
                "cloudy"
        ));

        // Act
        when(weatherRepository.findTop100ByCityContainingOrderByCreateAt("")).thenReturn((mockedListFromDb));
        WeatherListDto resultFromDb = weatherService.getWeatherBy("");

        // Assert
        assertEquals(mockedResult.getResult().get(0).getWeather(), resultFromDb.getResult().get(0).getWeather());
    }

    @Test
    void testListWeatherFromDatabase_WeatherNotFoundException() {
        // Arrange
        String city = "NonExistentCity";
        List<Weather> mockedListFromDb = null;

        // Act
        when(weatherRepository.findTop100ByCityContainingOrderByCreateAt(anyString())).thenReturn(mockedListFromDb);

        // Assert
        assertThrows(WeatherNotFoundException.class, () -> weatherService.getWeatherBy(city));
    }
}
