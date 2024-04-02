package org.example.weatherapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Weather {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;

    // db
    @JsonIgnore         // used to ignore this field in /search endpoint, otherwise an infinitive loop occurs!
    @ManyToOne
    private User user;

    // date when request is sent
    private LocalDateTime localDateTime;

    // stats
    private String nameOfCity;
    private String country;
    private double temperature;
    private String weather;


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
