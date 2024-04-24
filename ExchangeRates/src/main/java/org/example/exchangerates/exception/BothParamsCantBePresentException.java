package org.example.exchangerates.exception;

public class BothParamsCantBePresentException extends RuntimeException{
    public BothParamsCantBePresentException(final String message) {
        super(message);
    }
}
