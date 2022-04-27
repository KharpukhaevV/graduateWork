package ru.kharpukhaev.exceptions;

public class OverdueLoanException extends RuntimeException {
    public OverdueLoanException(String message) {
        super(message);
    }
}
