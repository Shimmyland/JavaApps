package org.example.projectmovierental.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRegistrationDTO {

    private UUID id;
    private String username;
}
