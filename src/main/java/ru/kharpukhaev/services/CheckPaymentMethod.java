package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;

@Service
public class CheckPaymentMethod {


    public boolean isCard(String number) {
        return number.length() == 16;
    }

    public boolean isAccount(String number) {
        return number.length() == 19;
    }
}
