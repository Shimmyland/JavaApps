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


    @Scheduled(cron = "0 1 * * * 1-5")   // (sec min hour dayOfMonth month dayOfWeek), practice - https://crontab.guru
    public void refreshRates(){
        rateService.getRates("CZK", null, null);  // try-catch
    }

    @Scheduled(cron = "1 * * * * *") // every minute
    public void refreshNumberOfRates(){
        long count = rateRepository.count();
        log.info("You have {} rates saved in database.", count);
    }


}
