package org.example.exchangerates.handler;

import org.example.exchangerates.dto.ResponseDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.exception.ParamsCantBePresentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParamsCantBePresentException.class)
    public ResponseEntity<ResponseDto> resolveException(ParamsCantBePresentException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ResponseDto> resolveException1(DuplicateException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> resolveException(NotFoundException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
