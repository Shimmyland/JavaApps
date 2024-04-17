package org.example.exchangerates.repository;

import org.example.exchangerates.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {
}
