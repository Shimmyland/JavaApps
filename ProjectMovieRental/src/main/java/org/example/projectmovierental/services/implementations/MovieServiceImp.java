package org.example.projectmovierental.services.implementations;

import org.example.projectmovierental.models.DTOs.MovieDTO;
import org.example.projectmovierental.models.DTOs.MovieResponseDTO;
import org.example.projectmovierental.models.Movie;
import org.example.projectmovierental.repositories.MovieRepository;
import org.example.projectmovierental.services.MovieAPI;
import org.example.projectmovierental.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

@Service
public class MovieServiceImp implements MovieService {

    // dependencies
    private final MovieRepository movieRepository;

    private MovieAPI movieAPI;

    // constance
    private final String key = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMjgyMWU1NTMzNWM5NjI2ZDkzMTkyNmVmMThjYzdmMyIsInN1YiI6IjU4ZTliZDhmOTI1MTQxNGIyODAyOWYxMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.TPUbd5lK-AsfrBp8c7E82KP55L0f2wptT_F65qaRmwY";
    private final String acceptValue = "application/json";

    // constructor
    @Autowired
    public MovieServiceImp(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieAPI = retrofit.create(MovieAPI.class);
    }

    // methods
    @Override
    public MovieResponseDTO getMovies(String title) {
        List<Movie> moviesFromDB = movieRepository.findAllByTitleContainingIgnoreCase(title);
        if (moviesFromDB.isEmpty()) {
            Call<MovieResponseDTO> sendRequest = movieAPI.fetchMovies(title, acceptValue, key);
            MovieResponseDTO result = null;

            try {
                Response<MovieResponseDTO> tryFetchData = sendRequest.execute();
                if (tryFetchData.isSuccessful() && tryFetchData.body() != null) {
                    result = tryFetchData.body();

                    // save it into DB
                    for (MovieDTO movieDTO : result.getResults()) {
                        movieRepository.save(new Movie(movieDTO));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result;

        } else {
            // there should be some way to make it more simple
            MovieResponseDTO movieResponseDTO = new MovieResponseDTO();
            for (Movie movie : moviesFromDB) {
                movieResponseDTO.add(new MovieDTO(movie));
            }
            return movieResponseDTO;
        }
    }
}
