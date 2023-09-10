package ru.krizhanovsky.aston.exception;

public class IncorrectPinException extends RuntimeException {
    public IncorrectPinException(String message) {
        super(message);
    }
}
