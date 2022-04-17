package ru.kharpukhaev.exceptions;

import ru.kharpukhaev.entity.Client;

public class InsufficientFunds extends RuntimeException {
    private Client sender;
    public InsufficientFunds(String message, Client sender) {
        super(message);
        this.sender = sender;
    }

    public Client getSender() {
        return sender;
    }
}
