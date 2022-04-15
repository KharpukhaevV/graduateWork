package ru.kharpukhaev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Client;

import java.util.HashMap;
import java.util.Map;

@Component
public class Transfer {

    private final Commissions commissions;

    @Autowired
    public Transfer(Commissions commissions) {
        this.commissions = commissions;
    }

    public Map<String, Client> doTransfer(Client sender, Client recipient, long sum) throws Exception {
        if (!commissions.checkCommission(sender.getCardNumber(), recipient.getCardNumber())) {
            long newSum = (long) (sum + commissions.calcCommission(sum));
            if (newSum <= sender.getBalance()) {
                recipient.setBalance(recipient.getBalance() + sum);
                sender.setBalance(sender.getBalance() - newSum);
            } else {
                throw new Exception();
            }
        } else {
            if (sum <= sender.getBalance()) {
                recipient.setBalance(recipient.getBalance() + sum);
                sender.setBalance(sender.getBalance() - sum);
            } else {
                throw new Exception();
            }
        }
        Map<String, Client> map = new HashMap<>();
        map.put("sender", sender);
        map.put("recipient", recipient);
        return map;
    }
}
