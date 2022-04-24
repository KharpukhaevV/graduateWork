package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.TransferRepository;

@Component
public class Transfer {
    private final Commissions commissions;
    private final FrodMonitor frodMonitor;
    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final CheckPaymentMethod checkPaymentMethod;

    @Autowired
    public Transfer(Commissions commissions, FrodMonitor frodMonitor, CardRepository cardRepository, TransferRepository transferRepository, AccountRepository accountRepository, CheckPaymentMethod checkPaymentMethod) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.checkPaymentMethod = checkPaymentMethod;
    }

    public void doTransfer(String senderNumber, String recipientNumber, long sum) throws InsufficientFunds {
//        Card recipient = cardRepository.findCardByNumber(recipientNumber);
//        if (recipient != null) {
//            if (sum <= sender.getBalance()) {
//                if (sender.getNumber().substring(0, 6).equals(recipientNumber.substring(0, 6))) {
//                    transferToClient(sender, recipientNumber, sum);
//                } else {
//                    transferToOther(sender, recipientNumber, sum);
//                }
//            } else {
//                throw new InsufficientFunds("Недостаточно средств.", sender);
//            }
//        } else {
//            throw new RecipientNotFound("Получатель с таким номером не найден");
//        }

    }

    private void transferToClient(Account cardSender, String recipientCardNumber, long sum) {
//        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
//        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
//        cardSender.setBalance(cardSender.getBalance() - sum);
//        cardRecipient.setBalance(cardRecipient.getBalance() + sum);
//        transferEntity.setStatus(TransferStatus.SUCCESS);
//        transferRepository.save(transferEntity);
//        if (checkPaymentMethod.isCard(cardSender.getNumber())) {
//            cardRepository.save((Card)cardSender);
//        }
//        cardRepository.save(cardRecipient);

    }

    private void transferToOther(Account cardSender, String recipientCardNumber, long sum) {
//        Card cardRecipient = cardRepository.findCardByNumber(recipientCardNumber);
//        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
//        long sumWithCommission = commissions.checkCommission(cardRecipient, sum) + sum;
//        cardSender.setBalance(cardSender.getBalance() - sumWithCommission);
//        cardRepository.save(cardSender);
//        if (frodMonitor.monitor(transferEntity)) {
//            cardRecipient.setBalance(cardRecipient.getBalance() + sum);
//            cardRepository.save(cardRecipient);
//        }
    }

    public void transferBetweenTheir(Card cardSender, Card cardRecipient, long sum) {
//        cardSender.setBalance(cardSender.getBalance() - sum);
//        cardRecipient.setBalance(cardRecipient.getBalance() + sum);
//        TransferEntity transferEntity = new TransferEntity(cardSender, cardRecipient, sum);
//        transferEntity.setStatus(TransferStatus.SUCCESS);
//        cardRepository.save(cardRecipient);
//        cardRepository.save(cardSender);
//        transferRepository.save(transferEntity);
    }
}
