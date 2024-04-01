package org.example.weatherapp.services;

import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.UserLoginDTO;
import org.example.weatherapp.models.DTOs.UserRegistrationDTO;
import org.example.weatherapp.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // used in weather service
    User find(UUID userId) throws Exception;

    // user registration
    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isUsernameInUse(String username);
    boolean isEmailInUse(String email);
    User save(UserRegistrationDTO userRegistrationDTO) throws MessagingException;
    boolean emailVerificationStatus();

    String encodedPassword(String password);

    // user email verification
    boolean usersEmailVerification(String token);


    // user login
    User findByUsername(String username);
    boolean validateCredentials(UserLoginDTO userLoginDTO);

    // JWT token generation
    String generateJwtToken(String username);

}
