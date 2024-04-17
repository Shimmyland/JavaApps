package org.example.exchangerates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.client.ClientApi;
import org.example.exchangerates.dto.ListOfCurrenciesDto;
import org.example.exchangerates.dto.StatusDto;
import org.example.exchangerates.exception.NotFoundException;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final ClientApi clientApi;
    private static final String API_KEY = System.getenv().get("apiKey");

    public Integer getStatus(){
        Call<StatusDto> request = clientApi.getStatus(API_KEY);
        try {
            Response<StatusDto> response = request.execute();
            if (!response.isSuccessful() || response.body() == null) {
                log.error("There is an issue with response in getStatus method.");
            }

            StatusDto statusDto = response.body();

            return statusDto.quotas().get("month").get("remaining");
        } catch (IOException e) {
            throw new NotFoundException("O ou, some error: " + e.getMessage());
        }
    }


}
