package ru.kharpukhaev.services;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Client;

@Component
public class Commissions {

    public long checkCommission(Client sender, Client recipient, long sum) {
        if (sender.getCardNumber().substring(0, 5).equals(recipient.getCardNumber().substring(0, 5))) {
            return sum;
        } else {
            return calcCommission(sum);
        }
    }

    public long calcCommission(long sum) {
        return (long) (sum * 0.1);
    }
}
