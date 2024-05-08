package org.example.exchangerates.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.service.RateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateScheduler {

    private final RateService rateService;

    // https://www.baeldung.com/slf4j-with-log4j2-logback
    // https://spring.io/guides/gs/scheduling-tasks
    // https://www.baeldung.com/spring-scheduled-tasks
    // (sec min hour dayOfMonth month dayOfWeek), practice - https://crontab.guru

    @Scheduled(cron = "${rate.scheduler.refreshRates.cron}")
    public void refreshRates(){
        try {
            rateService.getRates("CZK", "EUR,USD,GBP", null, null);
            rateService.getRates("USD", null, null, null);
            log.info("SCHEDULER: New rates saved in database.");
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
