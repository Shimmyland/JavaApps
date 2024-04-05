package org.example.weatherapp.controllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.models.DTOs.JwtTokenDTO;
import org.example.weatherapp.models.DTOs.ResponseDTO;
import org.example.weatherapp.models.DTOs.UserLoginDTO;
import org.example.weatherapp.models.DTOs.UserRegistrationDTO;
import org.example.weatherapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated                      // can be used BindingResult (contains errors based on @RequestBody entity)
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registration(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) throws MessagingException {
        userService.createNewUser(userRegistrationDTO);
        return ResponseEntity.ok(new ResponseDTO("User was created successfully."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ResponseDTO> verifyEmail(@RequestParam("token") String token) {
        userService.setEmailVerified(token);
        return ResponseEntity.ok(new ResponseDTO("Email was verified successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> userLogin(@Valid @RequestBody UserLoginDTO userLoginDTO){
        return ResponseEntity.ok(new JwtTokenDTO(userService.generateJwtToken(userLoginDTO)));
    }
}
