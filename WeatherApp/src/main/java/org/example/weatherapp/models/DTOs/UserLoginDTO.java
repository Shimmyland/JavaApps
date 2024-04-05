package org.example.weatherapp.models.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty(message = "Username is required.") String username,
        @NotEmpty(message = "Password is required.") String password
){}
