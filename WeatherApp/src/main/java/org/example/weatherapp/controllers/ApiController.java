package org.example.weatherapp.controllers;

import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.*;
import org.example.weatherapp.services.UserService;
import org.example.weatherapp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {

    // dependencies
    private final WeatherService weatherService;
    private final UserService userService;

    // constructor
    @Autowired
    public ApiController(WeatherService weatherService, UserService userService) {
        this.weatherService = weatherService;
        this.userService = userService;
    }

    // endpoints
    @PostMapping("/weather")    // weather search
    public ResponseEntity<WeatherResponseDTO> getLocalWeather(@RequestBody InputDTO inputDTO) throws Exception {
        WeatherResponseDTO weatherResponseDTO = weatherService.findWeather(inputDTO.getCity());
        if (weatherResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }

        weatherService.save(weatherResponseDTO, inputDTO.getId());
        return ResponseEntity.ok(weatherResponseDTO);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {
        // isEmailValid =
        // isPasswordValid =
        try {
            if (!userService.isEmailValid(userDTO.getEmail()) || !userService.isPasswordValid(userDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid Format"));
            }
            if (userService.isEmailInUse(userDTO.getEmail()) || userService.isUsernameInUse(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Username or email is already in use"));
            }
            return ResponseEntity.ok().body(new UserResponseDTO(userService.save(userDTO).getId(), userDTO.getUsername()));

        } catch (MessagingException e) {
            // error thrown by save method in userService when there is an issue with send verification email
            System.out.println("Messaging error during registration");
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("An error occurred while processing your request. Please try again later."));
        }
    }


    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        if (userService.usersEmailValidation(token)) {
            return ResponseEntity.ok("E-mail verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification token");
        }
    }
}
