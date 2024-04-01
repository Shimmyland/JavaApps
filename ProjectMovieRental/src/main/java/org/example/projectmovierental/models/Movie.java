package org.example.projectmovierental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectmovierental.models.DTOs.MovieDTO;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String releaseDate;

    @Column(columnDefinition = "longtext")  // specify the data type (default is varchar(255))
    private String overview;


    // custom constructors
    public Movie(String title, String releaseDate, String overview) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }
    public Movie(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.releaseDate = movieDTO.getReleaseDate();
        this.overview =movieDTO.getOverview();    // there is an error, that the string is actually too long for the field
    }
}
