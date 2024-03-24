package org.example.weatherapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Weather {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    // request sent
    private LocalDateTime localDateTime;

    // stats
    private String nameOfCity;
    private String country;
    private double temperature;
    private String weather;

    // db
    @ManyToOne
    private User user;


    // custom constructor
    public Weather(User user, String nameOfCity, String country, double temperature, String weather) {
        this.user = user;
        this.localDateTime = LocalDateTime.now();
        this.nameOfCity = nameOfCity;
        this.country = country;
        this.temperature = temperature;
        this.weather = weather;
    }
}
