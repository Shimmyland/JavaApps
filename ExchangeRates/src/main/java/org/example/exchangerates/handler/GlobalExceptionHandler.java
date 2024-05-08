package org.example.exchangerates.handler;

import org.example.exchangerates.dto.ResponseDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.InvalidInputException;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.exception.BothParamsCantBePresentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.DateTimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BothParamsCantBePresentException.class)
    public ResponseEntity<ResponseDto> resolveException(BothParamsCantBePresentException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ResponseDto> resolveException(DuplicateException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> resolveException(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ResponseDto> resolveException(DateTimeException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDto> resolveException(InvalidInputException e){
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> resolveException(){
        return ResponseEntity.badRequest().body(new ResponseDto("Unable to convert String input 'date' into LocalDate."));
    }

}
