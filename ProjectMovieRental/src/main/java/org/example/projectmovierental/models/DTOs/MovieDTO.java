package org.example.projectmovierental.models.DTOs;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectmovierental.models.Movie;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    private String overview;

    // custom constructor
    public MovieDTO(Movie movie) {
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.overview = movie.getOverview();
    }
}
