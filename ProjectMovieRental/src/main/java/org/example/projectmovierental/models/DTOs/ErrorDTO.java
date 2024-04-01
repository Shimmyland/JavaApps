package org.example.projectmovierental.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

    // used for error response from endpoints
    private String error;
}
