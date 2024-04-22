package org.example.exchangerates.exception;

public class ParamsCantBePresentException extends RuntimeException{
    public ParamsCantBePresentException(final String message) {
        super(message);
    }
}
