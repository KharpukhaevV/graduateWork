package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class Transfer {
    private final Commissions commissions;
    private final FrodMonitor frodMonitor;
    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    @Autowired
    public Transfer(Commissions commissions, FrodMonitor frodMonitor, CardRepository cardRepository, TransferRepository transferRepository) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
    }

    public void doTransfer(Card cardSender, String cardRecipient, long sum) throws InsufficientFunds {
        if (cardRecipient != null) {
            if (sum <= cardSender.getBalance()) {
                if (cardSender.getNumber().substring(0, 6).equals(cardRecipient.substring(0, 6))) {
                    transferToClient(cardSender, cardRecipient, sum);
                } else {
                    transferToOther(cardSender, cardRecipient, sum);
                }
            } else {
                throw new InsufficientFunds("Недостаточно средств.", cardSender);
            }
        } else {
            throw new RecipientNotFound("Получатель с таким номером карты не найден");
        }

    }

    private void transferToClient(Card cardSender, String recipientCardNumber, long sum) {
        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
        cardSender.setBalance(cardSender.getBalance() - sum);
        cardRecipient.setBalance(cardRecipient.getBalance() + sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        transferRepository.save(transferEntity);
        cardRepository.save(cardSender);
        cardRepository.save(cardRecipient);

    }

    private void transferToOther(Card cardSender, String recipientCardNumber, long sum) {
        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
        long sumWithCommission = commissions.checkCommission(cardRecipient, sum) + sum;
        cardSender.setBalance(cardSender.getBalance() - sumWithCommission);
        cardRepository.save(cardSender);
        if (frodMonitor.monitor(transferEntity)) {
            cardRecipient.setBalance(cardRecipient.getBalance() + sum);
            cardRepository.save(cardRecipient);
        }
    }

    public void transferBetweenTheir(Card cardSender, Card cardRecipient, long sum) {
        cardSender.setBalance(cardSender.getBalance() - sum);
        cardRecipient.setBalance(cardRecipient.getBalance() + sum);
        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        cardRepository.save(cardRecipient);
        cardRepository.save(cardSender);
        transferRepository.save(transferEntity);
    }
}
