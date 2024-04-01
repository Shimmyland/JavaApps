package org.example.weatherapp.controllers;

import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.*;
import org.example.weatherapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // dependencies
    private final UserService userService;

    // constructor
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // endpoints
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        // isEmailValid =
        // isPasswordValid =
        try {
            if (!userService.isEmailValid(userRegistrationDTO.getEmail()) || !userService.isPasswordValid(userRegistrationDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid Format"));
            }
            if (userService.isEmailInUse(userRegistrationDTO.getEmail()) || userService.isUsernameInUse(userRegistrationDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Username or email is already in use"));
            }
            return ResponseEntity.ok().body(new UserResponseDTO(userService.save(userRegistrationDTO).getId(), userRegistrationDTO.getUsername()));

        } catch (MessagingException e) {
            // error thrown by save method in userService when there is an issue with send verification email
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("An error occurred while processing your request. Please try again later."));
        }
    }

    @GetMapping("/verification")
    public ResponseEntity<String> emailVerification(@RequestParam("token") String token) {
        if (userService.usersEmailVerification(token)) {
            return ResponseEntity.ok("E-mail verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification token");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        if (userLoginDTO.getUsername().isEmpty() || userLoginDTO.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDTO("Field username and/or field password was empty!"));
        }
        if (!userService.validateCredentials(userLoginDTO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Username and/or password was incorrect!"));
        }
        if (!userService.findByUsername(userLoginDTO.getUsername()).isUserVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("User is not verified, " +
                    "please check your spam folder or click the 'Resend email verification' button"));
        }

        return ResponseEntity.ok().body(new JwtTokenDTO(userService.generateJwtToken(userLoginDTO.getUsername())));
    }
}
