package ru.kharpukhaev.services;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Client;

@Component
public class Commissions {

    public long checkCommission(Client sender, Client recipient, long sum) {
//        if (sender.getCard().getNumber().substring(0, 5).equals(recipient.getCard().getNumber().substring(0, 5))) {
//            return sum;
//        } else {
//            return calcCommission(sum);
//        }
        return 0;
    }

    public long calcCommission(long sum) {
        return (long) (sum * 0.1);
    }
}
