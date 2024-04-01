package org.example.weatherapp.services;

import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.UserDTO;
import org.example.weatherapp.models.User;

import java.util.UUID;

public interface UserService {

    // used in weather service
    User find(UUID id) throws Exception;

    // registration
    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isUsernameInUse(String username);
    boolean isEmailInUse(String email);
    User save(UserDTO userDTO) throws MessagingException;
    boolean emailVerificationStatus();

    // email validation
    boolean usersEmailValidation(String token);
}
