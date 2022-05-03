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
    private final CurrencyConvertService currencyConvertService;

    public TransferService(Commissions commissions,
                           FrodMonitor frodMonitor,
                           TransferRepository transferRepository,
                           AccountRepository accountRepository,
                           CreditService creditService,
                           CurrencyConvertService currencyConvertService) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.creditService = creditService;
        this.currencyConvertService = currencyConvertService;
    }

    @Transactional
    public void doTransfer(String senderNumber, String recipientNumber, Long sum) {
        Account sender = accountRepository.findAccountByNumber(senderNumber);
        Account recipient = accountRepository.findAccountByNumber(recipientNumber);
        Long newSum = currencyConvertService.checkCurrencyAndConvert(sender.getCurrency(), recipient.getCurrency(), sum);
        if (sender.getNumber().substring(0, 6).equals(recipientNumber.substring(0, 6))) {
            transferToClient(sender, recipient, newSum);
        } else {
            transferToOther(sender, recipient, newSum);
        }
    }

    @Transactional
    public void transferBetweenTheir(String senderNumber, String recipientNumber, Long sum) {
        Account sender = accountRepository.findAccountByNumber(senderNumber);
        Account recipient = accountRepository.findAccountByNumber(recipientNumber);
        checkCredit(sender, sum);
        sender.setBalance(sender.getBalance() - sum);
        recipient.setBalance(recipient.getBalance() + sum);
        TransferEntity transferEntity = new TransferEntity(sender, recipient, sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        accountRepository.save(recipient);
        accountRepository.save(sender);
        transferRepository.save(transferEntity);
    }

    private void transferToClient(Account sender, Account recipient, Long sum) {
        if (sender.getCard().getType().equals(CardType.CREDIT)) {
            creditService.addCredit(sum, sender.getCard());
        }
        TransferEntity transferEntity = new TransferEntity(sender, recipient, sum);
        checkCredit(sender, sum);
        sender.setBalance(sender.getBalance() - sum);
        recipient.setBalance(recipient.getBalance() + sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        transferRepository.save(transferEntity);
        accountRepository.save(sender);
        accountRepository.save(recipient);

    }

    private void transferToOther(Account sender, Account recipient, Long sum) {
        long sumWithCommission = commissions.checkCommission(recipient, sum) + sum;
        if (sender.getCard().getType().equals(CardType.CREDIT)) {
            creditService.addCredit(sumWithCommission, sender.getCard());
        }
        TransferEntity transferEntity = new TransferEntity(sender, recipient, sum);
        checkCredit(sender, sum);
        sender.setBalance(sender.getBalance() - sumWithCommission);
        accountRepository.save(sender);
        if (frodMonitor.monitor(transferEntity)) {
            recipient.setBalance(recipient.getBalance() + sum);
            accountRepository.save(recipient);
        }
    }

    private void checkCredit(Account sender, Long sum) {
        if (sender.getCard() != null) {
            if (sender.getCard().getType().equals(CardType.CREDIT)) {
                creditService.addCredit(sum, sender.getCard());
            }
        }
    }
}
