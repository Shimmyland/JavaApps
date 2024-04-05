package org.example.weatherapp.controllers;

import jakarta.mail.MessagingException;
import org.example.weatherapp.exceptions.IncorrectCredentialsException;
import org.example.weatherapp.exceptions.InvalidInputException;
import org.example.weatherapp.exceptions.UserNotFoundException;
import org.example.weatherapp.exceptions.UserNotVerifiedException;
import org.example.weatherapp.exceptions.WeatherNotFoundException;
import org.example.weatherapp.models.DTOs.ResponseDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> controllers(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDTO> weatherExceptionHandler_searchForWeather_1(){
        return ResponseEntity.badRequest().body(new ResponseDTO("Error, your request was not executed."));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseDTO> weatherExceptionHandler_searchForWeather_2(){
        return ResponseEntity.badRequest().body(new ResponseDTO("Error, request return response with empty body."));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDTO> weatherExceptionHandler_searchForWeather_3(){
        return ResponseEntity.badRequest().body(new ResponseDTO("Error, access denied to retried data."));
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<ResponseDTO> weatherExceptionHandler_searchForWeather_4(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("Error, weather based on your input was not found."));
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ResponseDTO> weatherExceptionHandler_listWeatherFromDatabase(){
        return ResponseEntity.badRequest().body(new ResponseDTO("Weather based on this city does not exist in database."));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> userExceptionHandler(){
        return ResponseEntity.badRequest().body(new ResponseDTO("User not found."));
    }


    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDTO> userExceptionHandler_registration_createNewUser_isUsernameOrEmailInUse(){
        return ResponseEntity.badRequest().body(new ResponseDTO("Username and/or email is already in use."));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ResponseDTO> userExceptionHandler_registration_createNewUser_sendVerificationEmail(){
        return ResponseEntity.badRequest().body(new ResponseDTO("There is an issue with sending verification email. Please try it."));
    }


    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ResponseDTO> userExceptionHandler_login_generateJwtToken_1(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Username and/or password was incorrect."));
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ResponseDTO> userExceptionHandler_login_generateJwtToken_2(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("User is not verified, please click on link in sent email."));
    }
}
