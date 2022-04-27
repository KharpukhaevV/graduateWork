package ru.kharpukhaev.exceptions;

import ru.kharpukhaev.entity.Account;

public class InsufficientFundsException extends RuntimeException {
    private Account sender;

    public InsufficientFundsException(String message, Account sender) {
        super(message);
        this.sender = sender;
    }

    public Account getSender() {
        return sender;
    }
}
