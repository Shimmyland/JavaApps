package org.example.weatherapp.models.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {      // it will throw MethodArgumentNotValidException by Spring MVC as a JSON response

    @NotEmpty(message = "Username is required.")
    private String username;

    @NotEmpty(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$", message = "Your password does not match required format.")
    private String password;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Surname is required.")
    private String surname;

    @NotEmpty(message = "Email is required.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Your email does not match required format.")
    private String email;
}

/*
// Define your record with the same fields and annotations as in the DTO
public record UserRegistrationDTO(
        @NotEmpty(message = "Username is required.") String username,
        @NotEmpty(message = "Password is required.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$",
                message = "Your password does not match required format.")
        String password,
        @NotEmpty(message = "Name is required.") String name,
        @NotEmpty(message = "Surname is required.") String surname,
        @NotEmpty(message = "Email is required.")
        @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Your email does not match required format.")
        String email
) {}
 */
