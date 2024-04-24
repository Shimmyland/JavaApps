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

    @Query(value = "SELECT * FROM currency", nativeQuery = true)
    List<Currency> findAllCurrencies();

    @Query(value = "SELECT * FROM currency WHERE type = ?1", nativeQuery = true)
    List<Currency> findAllByType(String type);

    Page<Currency> findAllBy(Pageable pageable);
}
