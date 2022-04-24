package ru.kharpukhaev.exceptions;

import ru.kharpukhaev.entity.Account;

public class InsufficientFunds extends RuntimeException {
    private Account sender;

    public InsufficientFunds(String message, Account sender) {
        super(message);
        this.sender = sender;
    }

    public Account getSender() {
        return sender;
    }
}
