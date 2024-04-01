package org.example.projectmovierental.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputSearchDTO {

    // DTO for search endpoint

    private String query;

}
