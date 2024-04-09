package org.example.weatherapp.repository;

import org.example.weatherapp.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<Weather, UUID> {

    List<Weather> findTop100ByCityContainingOrderByCreateAt(String city);
}
