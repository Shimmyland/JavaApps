package org.example.exchangerates.handler;

import org.example.exchangerates.dto.ResponseDto;
import org.example.exchangerates.exception.DuplicateException;
import org.example.exchangerates.exception.NotFoundException;
import org.example.exchangerates.exception.BothParamsCantBePresentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.DateTimeException;
import java.util.HashMap;

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
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<HashMap<String, String>> resolveException(DateTimeException e){
        HashMap<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
