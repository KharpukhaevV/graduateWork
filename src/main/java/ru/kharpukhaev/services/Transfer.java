package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
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

    private void doTransfer(Card cardSender, Card cardRecipient, long sum, double commission) throws InsufficientFunds {
        if (cardRecipient != null) {
            if (sum <= cardSender.getBalance()) {
                cardSender.setBalance((long) (cardSender.getBalance() - (sum + commission)));
                cardRepository.save(cardSender);
                TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
                if (frodMonitor.monitor(transferEntity)) {
                    cardRecipient.setBalance(cardRecipient.getBalance() + sum);
                    cardRepository.save(cardRecipient);
                }
            } else {
                throw new InsufficientFunds("Недостаточно средств.", cardSender);
            }
        } else {
            throw new RecipientNotFound("Получатель с таким номером карты не найден");
        }
    }

    public void transferToClient(Card cardSender, String recipientCardNumber, long sum) {
        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
        doTransfer(cardSender, cardRecipient, sum, 0);
    }

    public void transferToOther(Card cardSender, String recipientCardNumber, long sum) {
        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
        long commission = commissions.checkCommission(cardRecipient, sum);
        doTransfer(cardSender, cardRecipient, sum, commission);
    }

    public void transferBetweenTheir(Card cardSender, Card cardRecipient, long sum) {
        if (cardSender.getBalance() < sum) {
            throw new InsufficientFunds("Недостаточно средств.", cardSender);
        }
        cardSender.setBalance(cardSender.getBalance() - sum);
        cardRecipient.setBalance(cardRecipient.getBalance() + sum);
        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
        cardRepository.save(cardRecipient);
        cardRepository.save(cardSender);
        transferRepository.save(transferEntity);
    }
}
