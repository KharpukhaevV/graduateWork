package ru.kharpukhaev.service;

import org.springframework.stereotype.Component;

@Component
public class Commissions {

    public boolean checkCommission(String cardSender, String cardRecipient) {
        return cardSender.substring(0, 5).equals(cardRecipient.substring(0, 5));
    }

    public double calcCommission(long sum) {
        return sum * 0.1;
    }
}
