package ru.kharpukhaev.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.TransferRepository;

@Component
public class TransferService {
    private final Commissions commissions;
    private final FrodMonitor frodMonitor;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    private final CreditService creditService;

    public TransferService(Commissions commissions,
                           FrodMonitor frodMonitor,
                           TransferRepository transferRepository,
                           AccountRepository accountRepository,
                           CreditService creditService) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.creditService = creditService;
    }

    @Transactional
    public void doTransfer(String senderNumber, String recipientNumber, long sum) {
        Account sender = accountRepository.findAccountByNumber(senderNumber);
        if (sender.getNumber().substring(0, 6).equals(recipientNumber.substring(0, 6))) {
            transferToClient(sender, recipientNumber, sum);
        } else {
            transferToOther(sender, recipientNumber, sum);
        }
    }

    private void transferToClient(Account accountSender, String recipientNumber, long sum) {
        Account accountRecipient = accountRepository.findAccountByNumber(recipientNumber);
        if (accountSender.getCard().getType().equals(CardType.CREDIT)) {
            creditService.addCredit(sum, accountSender.getCard());
        }
        TransferEntity transferEntity = new TransferEntity(accountSender, accountRecipient, sum);
        accountSender.setBalance(accountSender.getBalance() - sum);
        accountRecipient.setBalance(accountRecipient.getBalance() + sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        transferRepository.save(transferEntity);
        accountRepository.save(accountSender);
        accountRepository.save(accountRecipient);

    }

    private void transferToOther(Account accountSender, String recipientNumber, long sum) {
        Account accountRecipient = accountRepository.findAccountByNumber(recipientNumber);
        long sumWithCommission = commissions.checkCommission(accountRecipient, sum) + sum;
        if (accountSender.getCard().getType().equals(CardType.CREDIT)) {
            creditService.addCredit(sumWithCommission, accountSender.getCard());
        }
        TransferEntity transferEntity = new TransferEntity(accountSender, accountRecipient, sum);
        accountSender.setBalance(accountSender.getBalance() - sumWithCommission);
        accountRepository.save(accountSender);
        if (frodMonitor.monitor(transferEntity)) {
            accountRecipient.setBalance(accountRecipient.getBalance() + sum);
            accountRepository.save(accountRecipient);
        }
    }

    @Transactional
    public void transferBetweenTheir(String senderNumber, String recipientNumber, long sum) {
        Account sender = accountRepository.findAccountByNumber(senderNumber);
        Account recipient = accountRepository.findAccountByNumber(recipientNumber);
        if (sender.getCard().getType().equals(CardType.CREDIT)) {
            creditService.addCredit(sum, sender.getCard());
        }
        sender.setBalance(sender.getBalance() - sum);
        recipient.setBalance(recipient.getBalance() + sum);
        TransferEntity transferEntity = new TransferEntity(sender, recipient, sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        accountRepository.save(recipient);
        accountRepository.save(sender);
        transferRepository.save(transferEntity);
    }
}
