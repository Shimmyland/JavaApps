package org.example.weatherapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
// @Builder + @AllArgsConstructor
// @EqualsAndHashCode   !
// @ToString    !
// @Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    private String city;
    private String country;
    private double temperature;
    private String forecast;

    public Weather(String city, String country, double temperature, String forecast) {
        this.createAt = LocalDateTime.now();
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.forecast = forecast;
    }
}
