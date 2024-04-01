package org.example.projectmovierental.controllers;

import org.example.projectmovierental.models.DTOs.ErrorDTO;
import org.example.projectmovierental.models.DTOs.InputRegistrationDTO;
import org.example.projectmovierental.models.DTOs.ResponseRegistrationDTO;
import org.example.projectmovierental.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> registration(@RequestBody InputRegistrationDTO inputRegistrationDTO){
        if (!userService.isInputValid(inputRegistrationDTO)){   // email or password
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Email and/or password has invalid format"));
        }

        if (userService.isInputInUse(inputRegistrationDTO)){    // email or username
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Username and/or password is already in use!"));
        }

        return ResponseEntity.ok(new ResponseRegistrationDTO(userService.save(inputRegistrationDTO).getId(), inputRegistrationDTO.getUsername()));
    }
}
