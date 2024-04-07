package org.example.weatherapp.handler;

import org.example.weatherapp.exception.WeatherNotFoundException;
import org.example.weatherapp.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> controllers(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<ErrorDTO> weatherExceptionHandler_searchForWeather_4(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Error, weather based on your input was not found."));
    }
}
