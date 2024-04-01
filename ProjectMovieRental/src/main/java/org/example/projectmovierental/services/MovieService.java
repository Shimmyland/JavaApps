package org.example.projectmovierental.services;

import org.example.projectmovierental.models.DTOs.MovieResponseDTO;

public interface MovieService {

    MovieResponseDTO getMovies(String title);
}
