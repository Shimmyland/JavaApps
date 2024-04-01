package org.example.weatherapp.repositories;

import org.example.weatherapp.models.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, UUID> {

    List<Weather> findAllByWeatherIgnoreCaseContaining(String weather);
    List<Weather> findAllByCountryIgnoreCaseContaining(String country);
    List<Weather> findAllByNameOfCityIgnoreCaseContaining(String nameOfCity);

}
