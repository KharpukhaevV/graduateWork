package ru.kharpukhaev.exceptions;

import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;

public class InsufficientFunds extends RuntimeException {
    private Card sender;
    public InsufficientFunds(String message, Card sender) {
        super(message);
        this.sender = sender;
    }

    public Card getSender() {
        return sender;
    }
}
