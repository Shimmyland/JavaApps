package org.example.usersetup.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserSignInDTO(
        @NotEmpty(message = "Username is required.") String username,
        @NotEmpty(message = "Password is required.") String password
) {
}
