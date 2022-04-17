package ru.kharpukhaev.exceptions;

public class RecipientNotFound extends RuntimeException {
    public RecipientNotFound(String message) {
        super(message);
    }
}
