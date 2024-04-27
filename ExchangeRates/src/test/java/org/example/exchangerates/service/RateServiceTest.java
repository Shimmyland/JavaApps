package org.example.exchangerates.service;

import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.RatesDto;
import org.example.exchangerates.entity.Currency;
import org.example.exchangerates.entity.Rate;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        Currency mockedCurrency = Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build();

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

    @Test
    void getRates_successful_allParams() {
        List<Rate> mockedRates = new ArrayList<>();
        Rate rate1 = Rate.builder()
                .fromDate(LocalDate.ofEpochDay(2024 - 04 - 23))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("USD").name("testing").symbol("us").type("fiat").build())
                .price(1)
                .build();
        Rate rate2 = Rate.builder()
                .fromDate(LocalDate.ofEpochDay(2024 - 04 - 23))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("EUR").name("testing").symbol("eu").type("fiat").build())
                .price(2)
                .build();
        mockedRates.add(rate1);
        mockedRates.add(rate2);

        HashMap<String, RatesDto.RateDto> data = new HashMap<>();
        RatesDto.RateDto rateDto = new RatesDto.RateDto("USD", 1);
        data.put("USD", rateDto);
        RatesDto.RateDto rateDto1 = new RatesDto.RateDto("EUR", 2);
        data.put("EUR", rateDto1);
        HashMap<String, String> meta = new HashMap<>();
        meta.put("from_date", "2024-04-23");
        meta.put("base_currency", "CZK");
        RatesDto mockedResult = new RatesDto(meta, data);

        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(anyString(), anyString(), any(LocalDate.class))).thenReturn(mockedRates);

        assertEquals(mockedResult, rateService.getRates("CZK", "fiat", "2024-04-23"));
    }

    @Test
    void getRates_successful_ratesNotFound() {
        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(anyString(), anyString(), any(LocalDate.class))).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> rateService.getRates("CZK", "fiat", "2024-04-23"));
    }


}
