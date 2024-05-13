package org.example.exchangerates.service;

import org.example.exchangerates.client.CurrencyApiClient;
import org.example.exchangerates.config.properties.CurrencyApiProperties;
import org.example.exchangerates.dto.CurrenciesDto;
import org.example.exchangerates.entity.Currency;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.repository.CurrencyRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    CurrencyApiClient currencyApiClient;

    @Mock
    CurrencyApiProperties currencyApiProperties;

    @InjectMocks
    CurrencyService currencyService;

    @Test
    void findCurrencyBy_successful() {

        final Currency currency = Currency.builder()
                .code("TEST")
                .name("Testing currency")
                .symbol("Test")
                .type("virtual")
                .build();

        when(currencyRepository.findByCode(anyString())).thenReturn(Optional.of(currency));

        assertEquals(currency, currencyService.findCurrencyBy("TEST"));
    }

    @Test
    void findCurrencyBy_unsuccessful() {
        when(currencyRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> currencyService.findCurrencyBy("TEST"));
    }

    @Test
    void modelData_successful() {
        List<Currency> mockCurrencies = List.of(Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build());

        LinkedHashMap<String, CurrenciesDto.CurrencyDto> expectedMap = new LinkedHashMap<>();
        expectedMap.put("TEST", new CurrenciesDto.CurrencyDto("TEST", "Testing currency", "Test", "virtual"));
        CurrenciesDto expected = new CurrenciesDto(expectedMap);

        assertEquals(expected, currencyService.modelData(mockCurrencies));
    }

    @Test
    void modelData_empty() {
        List<Currency> mockCurrencies = Collections.emptyList();

        CurrenciesDto result = currencyService.modelData(mockCurrencies);

        assertTrue(result.data().isEmpty());
    }

    @Test
    void getAllCurrencies_successful() {
        List<Currency> mockCurrencies = List.of(Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build());

        LinkedHashMap<String, CurrenciesDto.CurrencyDto> mockMap = new LinkedHashMap<>();
        mockMap.put("TEST", new CurrenciesDto.CurrencyDto("TEST", "Testing currency", "Test", "virtual"));
        CurrenciesDto mockDto = new CurrenciesDto(mockMap);

        when(currencyRepository.findAllCurrencies()).thenReturn(mockCurrencies);
        // when(currencyService.modelData(anyList())).thenReturn(mockDto);

        assertEquals(mockDto, currencyService.getAllCurrencies());
    }

    @Test
    void getSpecificCurrency_successful() {
        Currency mockCurrency = Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build();

        LinkedHashMap<String, CurrenciesDto.CurrencyDto> expectedMap = new LinkedHashMap<>();
        expectedMap.put("TEST", new CurrenciesDto.CurrencyDto("TEST", "Testing currency", "Test", "virtual"));
        CurrenciesDto expected = new CurrenciesDto(expectedMap);

        when(currencyRepository.findByCode(anyString())).thenReturn(Optional.of(mockCurrency));
        // when(currencyService.modelData(anyList())).thenReturn(expected);

        assertEquals(expected, currencyService.getSpecificCurrency("TEST"));
    }

    @Test
    void getCurrenciesBy_successful() {
        List<Currency> mockCurrencies = List.of(Currency.builder().code("TEST").name("Testing currency").symbol("Test").type("virtual").build());

        LinkedHashMap<String, CurrenciesDto.CurrencyDto> expectedMap = new LinkedHashMap<>();
        expectedMap.put("TEST", new CurrenciesDto.CurrencyDto("TEST", "Testing currency", "Test", "virtual"));
        CurrenciesDto expected = new CurrenciesDto(expectedMap);

        when(currencyRepository.findAllByType(anyString())).thenReturn(mockCurrencies);
        // when(currencyService.modelData(anyList())).thenReturn(expected);


        assertEquals(expected, currencyService.getCurrenciesBy("virtual"));
    }

    @Test
    void setNewCurrencies_successful() throws IOException {
        var request = mock(Call.class);
        LinkedHashMap<String, CurrenciesDto.CurrencyDto> expectedMap = new LinkedHashMap<>();
        expectedMap.put("TEST", new CurrenciesDto.CurrencyDto("TEST", "Testing currency", "Test", "virtual"));
        CurrenciesDto expected = new CurrenciesDto(expectedMap);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllCurrencies(anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(expected));
        when(currencyRepository.findAllCodes()).thenReturn(Collections.emptySet());
        when(currencyRepository.save(any(Currency.class))).thenReturn(new Currency());

        assertEquals(1, currencyService.setNewCurrencies());
    }

    @Test
    void setNewCurrencies_currencyNotFound() throws IOException {
        var request = mock(Call.class);

        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllCurrencies(anyString())).thenReturn(request);
        when(request.execute()).thenReturn(Response.success(null));

        assertThrows(NotFoundException.class, () -> currencyService.setNewCurrencies());
    }

    @Test
    void setNewCurrencies_ioException() throws IOException {
        var request = mock(Call.class);
        when(currencyApiProperties.getAccessKey()).thenReturn("testAccessKey");
        when(currencyApiClient.getAllCurrencies(anyString())).thenReturn(request);
        when(request.execute()).thenThrow(IOException.class);

        assertThrows(NotFoundException.class, () -> currencyService.setNewCurrencies());
    }

}
