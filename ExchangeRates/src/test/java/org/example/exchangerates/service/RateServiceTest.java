package org.example.exchangerates.service;

import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.entity.Currency;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.RateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

    @Mock
    RateRepository rateRepository;

    @Mock
    CurrencyApiClient currencyApiClient;

    @Mock
    CurrencyApiProperties currencyApiProperties;

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    RateService rateService;


    @Test
    void setRates_successful() throws IOException {
        var request = mock(Call.class);
        HashMap<String, String> mockedMeta = new HashMap<>();
        mockedMeta.put("last_updated_at", "2024-04-19");
        HashMap<String, RatesDto.RateDto> mockedData = new HashMap<>();
        mockedData.put("TEST", new RatesDto.RateDto("TEST", 1.0));
        RatesDto mockedResponse = new RatesDto(mockedMeta, mockedData);
        Currency mockedCurrency = new Currency("TEST", "Testing currency", "Test", "virtual");

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(mockedResponse));
        when(currencyService.findCurrencyBy(anyString())).thenReturn(mockedCurrency);
        when(rateRepository.existsByParams(anyString(), anyString(), any(LocalDate.class))).thenReturn(false);
        // when(rateRepository.save(any(Rate.class))).thenReturn();

        assertEquals(mockedResponse, rateService.setRates("TEST1", "TEST2", "TEST3"));
    }

    @Test
    void setRates_currencyNotFound() throws IOException {
        var request = mock(Call.class);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(null));

        assertThrows(NotFoundException.class, () -> rateService.setRates("TEST1", "TEST2", "TEST3"));
    }

    @Test
    void setRates_ioException() throws IOException {
        var request = mock(Call.class);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenThrow(IOException.class);

        assertThrows(NotFoundException.class, () -> rateService.setRates("TEST1", "TEST2", "TEST3"));
    }
}
