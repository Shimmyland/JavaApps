package org.example.exchangerates.repository;

import org.example.exchangerates.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT 1 FROM rate WHERE base_currency_id = (SELECT id FROM currency WHERE code = ?1) " +
            "AND currency_id = (SELECT id FROM currency WHERE code = ?2) AND from_date = ?3) THEN 'yes' ELSE 'no' END", nativeQuery = true)
    boolean existsByParams(String baseCurrencyCode, String currencyCode, LocalDate fromDate);
    List<Rate> findAllByBaseCurrency_CodeAndFromDate(String baseCurrencyCode, LocalDate fromDate);
    List<Rate> findAllByBaseCurrency_CodeAndCurrency_TypeAndFromDate(String baseCurrencyCode, String currencyType, LocalDate fromDate);
    @Query(value = "SELECT MAX(from_date) FROM rate", nativeQuery = true)
    LocalDate findByFromDate();
}
