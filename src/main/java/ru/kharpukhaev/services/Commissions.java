package ru.kharpukhaev.services;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;

@Component
public class Commissions {

    public long checkCommission(Card recipient, long sum) {
        switch (recipient.getNumber().substring(0, 6)) {
            case ("123456"): return (long)(sum * 0.3);
            case ("654321"): return (long)(sum * 0.2);
            default: return (long) (sum * 0.1);
        }
    }
}
