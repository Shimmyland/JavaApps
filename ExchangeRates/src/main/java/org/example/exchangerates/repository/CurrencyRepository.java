package org.example.exchangerates.repository;

import org.example.exchangerates.entity.Cur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Cur, UUID> {

    Optional<Cur> findByCode(String code);

    @Query("SELECT code FROM Cur code")
    Set<String> findAllCodes();

}
