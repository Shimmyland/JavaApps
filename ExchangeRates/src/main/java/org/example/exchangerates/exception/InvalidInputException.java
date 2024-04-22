package org.example.exchangerates.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(final String message) {
        super(message);
    }
}
