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
    void ratesFromDatabase_all_successful() {
        List<Rate> mockedRates = new ArrayList<>();
        Rate rate = Rate.builder()
                .fromDate(LocalDate.of(2024, 4, 19))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("USD").name("testing").symbol("us").type("fiat").build())
                .price(1)
                .build();
        mockedRates.add(rate);

        when(rateRepository.findAllByBaseCurrency_CodeAndFromDate(anyString(), any(LocalDate.class))).thenReturn(mockedRates);
        when(currencyService.countAllCurrencies()).thenReturn(1);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", null, null, LocalDate.of(2024, 4, 19)));
    }

    @Test
    void ratesFromDatabase_all_empty() {
        List<Rate> mockedRates = new ArrayList<>();

        when(rateRepository.findAllByBaseCurrency_CodeAndFromDate(anyString(), any(LocalDate.class))).thenReturn(mockedRates);
        when(currencyService.countAllCurrencies()).thenReturn(1);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", null, null, LocalDate.of(2024, 4, 19)));
    }

    @Test
    void ratesFromDatabase_currencies_successful() {
        List<Rate> mockedRates = new ArrayList<>();
        Rate rate1 = Rate.builder()
                .fromDate(LocalDate.of(2024, 4, 19))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("EUR").name("testing").symbol("eur").type("fiat").build())
                .price(1)
                .build();
        mockedRates.add(rate1);

        when(currencyService.countCurrencies(any(List.class))).thenReturn(1);
        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_CodeInAndFromDate(anyString(), any(List.class), any(LocalDate.class))).thenReturn(mockedRates);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", "EUR", null, LocalDate.of(2024, 4, 19)));
    }

    @Test
    void ratesFromDatabase_currencies_empty() {
        List<Rate> mockedRates = new ArrayList<>();

        when(currencyService.countCurrencies(any(List.class))).thenReturn(1);
        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_CodeInAndFromDate(anyString(), any(List.class), any(LocalDate.class))).thenReturn(mockedRates);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", "EUR", null, LocalDate.of(2024, 4, 19)));
    }

    @Test
    void ratesFromDatabase_type_successful() {
        List<Rate> mockedRates = new ArrayList<>();
        Rate rate1 = Rate.builder()
                .fromDate(LocalDate.of(2024, 4, 19))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("EUR").name("testing").symbol("eur").type("fiat").build())
                .price(1)
                .build();
        mockedRates.add(rate1);

        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(anyString(), anyString(), any(LocalDate.class))).thenReturn(mockedRates);
        when(currencyService.countAllCurrenciesByType(anyString())).thenReturn(1);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", null, "fiat", LocalDate.of(2024, 4, 19)));
    }

    @Test
    void ratesFromDatabase_type_empty() {
        List<Rate> mockedRates = new ArrayList<>();

        when(rateRepository.findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(anyString(), anyString(), any(LocalDate.class))).thenReturn(mockedRates);
        when(currencyService.countAllCurrenciesByType(anyString())).thenReturn(1);

        assertEquals(mockedRates, rateService.ratesFromDatabase("CZK", null, "fiat", LocalDate.of(2024, 4, 19)));
    }

    @Test
    void modelData_successful() {
        List<Rate> mockedRates = new ArrayList<>();
        Rate rate1 = Rate.builder()
                .fromDate(LocalDate.of(2024,4,19))
                .baseCurrency(Currency.builder().code("CZK").name("testing").symbol("kč").type("fiat").build())
                .currency(Currency.builder().code("USD").name("testing").symbol("us").type("fiat").build())
                .price(1)
                .build();
        mockedRates.add(rate1);

        HashMap<String, String> meta = new HashMap<>();
        meta.put("from_date", LocalDate.of(2024,4,19).toString());
        meta.put("base_currency", "USD");
        HashMap<String, RatesDto.RateDto> data = new HashMap<>();
        for (Rate rate : mockedRates) {
            RatesDto.RateDto rateDto = new RatesDto.RateDto(rate.getCurrency().getCode(), rate.getPrice());
            data.put(rate.getCurrency().getCode(), rateDto);
        }
        RatesDto ratesDto = new RatesDto(meta, data);

        assertEquals(ratesDto, rateService.modelData(mockedRates, LocalDate.of(2024,4,19), "USD"));
    }

    @Test
    void fetchAndSaveData_successful() throws IOException {
        var request = mock(Call.class);
        HashMap<String, String> mockedMeta = new HashMap<>();
        mockedMeta.put("last_updated_at", "2024-04-19");
        HashMap<String, RatesDto.RateDto> mockedData = new HashMap<>();
        mockedData.put("TEST", new RatesDto.RateDto("TEST", 1.0));
        RatesDto mockedResponse = new RatesDto(mockedMeta, mockedData);
        Currency mockedCurrency = Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build();

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(mockedResponse));
        when(rateRepository.existsByParams(anyString(), anyString(), any(LocalDate.class))).thenReturn(false);
        when(currencyService.findCurrencyBy(anyString())).thenReturn(mockedCurrency);


        assertEquals(mockedResponse, rateService.fetchAndSaveData("CZK", "null", "null", LocalDate.of(2024,4,19)));
    }

    @Test
    void fetchAndSaveData_currencyNotFound() throws IOException {
        var request = mock(Call.class);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(null));

        assertThrows(NotFoundException.class, () -> rateService.fetchAndSaveData("CZK", "null", "null", LocalDate.of(2024,4,19)));
    }

    @Test
    void fetchAndSaveData_ioException() throws IOException {
        var request = mock(Call.class);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllRates(anyString(), any(LocalDate.class), anyString(), anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenThrow(IOException.class);

        assertThrows(NotFoundException.class, () -> rateService.fetchAndSaveData("CZK", "null", "null", LocalDate.of(2024,4,19)));
    }
}
