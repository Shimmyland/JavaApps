package org.example.weatherapp.repositories;

import org.example.weatherapp.models.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<Weather, UUID> {

    // List<Weather> findTop100ByCityContaining(String city);
    Optional<List<Weather>> findByUserId(UUID userId);
}
