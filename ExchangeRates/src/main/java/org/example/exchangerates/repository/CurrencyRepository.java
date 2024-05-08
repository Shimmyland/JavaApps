package org.example.exchangerates.repository;

import org.example.exchangerates.entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

    Optional<Currency> findByCode(String code);

    @Query(value = "SELECT code FROM currency", nativeQuery = true)
    Set<String> findAllCodes();

    @Query(value = "SELECT * FROM currency ORDER BY code", nativeQuery = true)
    List<Currency> findAllCurrencies();

    @Query(value = "SELECT * FROM currency WHERE type = ?1 ORDER BY code", nativeQuery = true)
    List<Currency> findAllByType(String type);

    Page<Currency> findAllByOrderByCodeAsc(Pageable pageable);


    @Query(value = "SELECT COUNT(id) FROM currency", nativeQuery = true)
    int countAll();

    @Query(value = "SELECT COUNT(id) FROM currency WHERE type = ?1", nativeQuery = true)
    int countByType(String type);

    @Query(value = "SELECT COUNT(id) FROM currency WHERE code IN ?1", nativeQuery = true)
    int countSpecificCurrencies(List<String> currencies);
}
