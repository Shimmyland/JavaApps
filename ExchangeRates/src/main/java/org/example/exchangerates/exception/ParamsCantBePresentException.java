package org.example.exchangerates.exception;

public class ParamsCantBePresentException extends RuntimeException{
    public ParamsCantBePresentException(String message) {
        super(message);
    }
}
