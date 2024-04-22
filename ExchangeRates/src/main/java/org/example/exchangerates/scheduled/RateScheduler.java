package org.example.exchangerates.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exchangerates.repository.RateRepository;
import org.example.exchangerates.service.RateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateScheduler {

    private final RateService rateService;
    private final RateRepository rateRepository;

    // https://www.baeldung.com/slf4j-with-log4j2-logback
    // https://spring.io/guides/gs/scheduling-tasks
    // https://www.baeldung.com/spring-scheduled-tasks
    // (sec min hour dayOfMonth month dayOfWeek), practice - https://crontab.guru

    @Scheduled(cron = "${rate.scheduler.refreshRates.cron}")
    public void refreshRates(){
        try {
            rateService.setRates("CZK", null, null);
            log.info("You have {} rates saved in database.", rateRepository.count());
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
