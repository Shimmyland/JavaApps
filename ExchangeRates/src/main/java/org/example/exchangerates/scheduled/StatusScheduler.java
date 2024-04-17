package org.example.exchangerates.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.service.StatusService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatusScheduler {

    private final StatusService statusService;

    @Scheduled(cron = "0 23 * * * *")
    private void refreshStatus(){
        log.info("Your remaining number of Calls to 3rd-party API is {} until the end of the month.", statusService.getStatus());
    }
}
