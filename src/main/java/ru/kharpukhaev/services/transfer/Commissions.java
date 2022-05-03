package ru.kharpukhaev.services.transfer;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Account;

@Component
public class Commissions {

    public long checkCommission(Account recipient, long sum) {
        return switch (recipient.getNumber().substring(0, 6)) {
            case ("123456") -> (long) (sum * 0.3);
            case ("654321") -> (long) (sum * 0.2);
            default -> (long) (sum * 0.1);
        };
    }
}
