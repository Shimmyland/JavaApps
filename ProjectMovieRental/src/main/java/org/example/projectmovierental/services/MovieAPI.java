package org.example.projectmovierental.services;

import org.example.projectmovierental.models.DTOs.MovieResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("movie")
    Call<MovieResponseDTO> fetchMovies(@Query("query") String title, @Header("accept") String acceptValue, @Header("Authorization") String key);

}
