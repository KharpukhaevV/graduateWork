package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
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

    public void doTransfer(Client sender, String recipientAccountNumber, long sum) throws InsufficientFunds {
        Client recipient = clientRepository.findByCardNumber(recipientAccountNumber);
        if (recipient != null) {
            long newSum = commissions.checkCommission(sender, recipient, sum);
            if (newSum <= sender.getBalance()) {
                sender.setBalance(sender.getBalance() - newSum);
                clientRepository.save(sender);
                TransferEntity transferEntity = new TransferEntity(sender, recipient.getAccountNumber(), sum);
                if (frodMonitor.monitor(transferEntity)) {
                    recipient.setBalance(recipient.getBalance() + sum);
                }
            } else {
                throw new InsufficientFunds("Недостаточно средств.", sender);
            }
        } else {
            throw new RecipientNotFound("Получатель с таким номером не найден");
        }
    }
}
