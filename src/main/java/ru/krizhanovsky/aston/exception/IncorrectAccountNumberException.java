package ru.krizhanovsky.aston.exception;

public class IncorrectAccountNumberException extends RuntimeException {
    public IncorrectAccountNumberException(String message) {
        super(message);
    }
}
