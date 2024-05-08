package org.example.usersetup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.usersetup.dto.JwtTokenDTO;
import org.example.usersetup.dto.ResponseDTO;
import org.example.usersetup.dto.UserSignUpDTO;
import org.example.usersetup.dto.UserSignInDTO;
import org.example.usersetup.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor            // can be used BindingResult (contains errors based on @RequestBody entity)
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @PostMapping("/user-sign-up")
    public ResponseEntity<ResponseDTO> userSignUp(@Valid @RequestBody final UserSignUpDTO userSignUpDTO) {
        userService.createUser(userSignUpDTO);
        return ResponseEntity.ok(new ResponseDTO("User creation was successful."));
    }

    @GetMapping("/user-sign-in")
    public ResponseEntity<JwtTokenDTO> userSignIn(@Valid @RequestBody final UserSignInDTO userSignInDTO){
        return ResponseEntity.ok(new JwtTokenDTO(userService.verifyUsersCredentials(userSignInDTO)));
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<ResponseDTO> verifyEmail(@PathVariable final String token) {
        userService.setUsersToken(token);
        return ResponseEntity.ok(new ResponseDTO("Email was verified successfully."));
    }



}
