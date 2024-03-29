package org.example.projectlibrary.controllers;

import org.example.projectlibrary.models.DTOs.ErrorDTO;
import org.example.projectlibrary.models.DTOs.UserDTO;
import org.example.projectlibrary.models.DTOs.UserResultDTO;
import org.example.projectlibrary.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    // dependencies
    private final UserService userService;

    // constructor
    public MainController(UserService userService) {
        this.userService = userService;
    }

    // endpoints
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO){
        if (!userService.isEmailValid(userDTO.getEmail()) || !userService.isPasswordValid(userDTO.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid Format"));
        }
        if (userService.isEmailInUse(userDTO.getEmail()) || userService.isUsernameInUse(userDTO.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Username or email is already in use"));
        }
        return ResponseEntity.ok().body(new UserResultDTO(userService.save(userDTO).getId(), userDTO.getUsername()));

    }



}
