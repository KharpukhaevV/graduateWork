package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class Transfer {

    private final ClientRepository clientRepository;
    private final Commissions commissions;
    private final FrodMonitor frodMonitor;

    @Autowired
    public Transfer(ClientRepository clientRepository, Commissions commissions, FrodMonitor frodMonitor) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.clientRepository = clientRepository;
    }

    public void doTransfer(Client sender, Client recipient, long sum) throws Exception {
        if (!commissions.checkCommission(sender.getCardNumber(), recipient.getCardNumber())) {
            long newSum = (long) (sum + commissions.calcCommission(sum));
            if (newSum <= sender.getBalance()) {
                sender.setBalance(sender.getBalance() - newSum);
                clientRepository.save(sender);
                TransferEntity transferEntity = new TransferEntity(sender, recipient.getAccountNumber(), sum);
                if (frodMonitor.monitor(transferEntity)) {
                    recipient.setBalance(recipient.getBalance() + sum);
                }
            } else {
                throw new Exception();
            }
        } else {
            if (sum <= sender.getBalance()) {
                sender.setBalance(sender.getBalance() - sum);
                recipient.setBalance(recipient.getBalance() + sum);
            } else {
                throw new Exception();
            }
        }
    }
}