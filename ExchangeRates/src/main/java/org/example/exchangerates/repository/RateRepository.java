package org.example.exchangerates.repository;

import org.example.exchangerates.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT 1 FROM rate WHERE base_currency_id = (SELECT id FROM currency WHERE code = ?1) " +
            "AND currency_id = (SELECT id FROM currency WHERE code = ?2) AND from_date = ?3) THEN 'yes' ELSE 'no' END", nativeQuery = true)
    boolean existsByParams(String baseCurrencyCode, String currencyCode, LocalDate fromDate);

}
