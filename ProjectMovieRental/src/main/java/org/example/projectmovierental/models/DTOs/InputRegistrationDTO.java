package org.example.projectmovierental.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputRegistrationDTO {

    // DTO for (user) registration endpoint

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
}
