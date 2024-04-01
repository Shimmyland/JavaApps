package org.example.projectmovierental.controllers;

import org.example.projectmovierental.models.DTOs.ErrorDTO;
import org.example.projectmovierental.models.DTOs.MovieResponseDTO;
import org.example.projectmovierental.models.DTOs.InputSearchDTO;
import org.example.projectmovierental.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    // dependencies
    private final MovieService movieService;

    // constructor
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    // endpoints
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody InputSearchDTO inputSearchDTO){
        MovieResponseDTO movieResponseDTO = movieService.getMovies(inputSearchDTO.getQuery());
        if (movieResponseDTO.getResults().isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDTO("No movies found matching the search."));
        } else {
            return ResponseEntity.ok(movieResponseDTO);
        }
    }


}
