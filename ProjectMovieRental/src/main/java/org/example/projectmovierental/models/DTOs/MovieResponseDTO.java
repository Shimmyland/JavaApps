package org.example.projectmovierental.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO {

    List<MovieDTO> results = new ArrayList<>();

    public void add(MovieDTO movieDTO) {
        this.results.add(movieDTO);
    }
}
