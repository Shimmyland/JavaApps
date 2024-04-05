package org.example.weatherapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor      // pro Hibernate / Spring ?
@Entity
// @Builder + @AllArgsConstructor
// @EqualsAndHashCode
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    private String city;
    private String country;
    private double temperature;
    private String weather;


    public Weather(User user, String city, String country, double temperature, String weather) {
        this.user = user;
        this.createAt = LocalDateTime.now();
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.weather = weather;
    }
}
