package ru.kharpukhaev.services;

import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.exceptions.InsufficientFundsException;
import ru.kharpukhaev.exceptions.RecipientNotFoundException;
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

    public void doTransfer(String senderNumber, String recipientNumber, long sum) throws InsufficientFundsException {
        Account sender = accountRepository.findAccountByNumber(senderNumber);
        if (sender.getNumber().substring(0, 6).equals(recipientNumber.substring(0, 6))) {
            transferToClient(sender, recipientNumber, sum);
        } else {
            transferToOther(sender, recipientNumber, sum);
        }
    }

    private void transferToClient(Account accountSender, String recipientNumber, long sum) {
        checkSum(accountSender, sum);
        Account accountRecipient = checkRecipient(recipientNumber);
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
        Account accountRecipient = checkRecipient(recipientNumber);
        long sumWithCommission = commissions.checkCommission(accountRecipient, sum) + sum;
        checkSum(accountSender, sumWithCommission);
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

    public void transferBetweenTheir(Account sender, Account recipient, long sum) {
        checkSum(sender, sum);
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

    private void checkSum(Account sender, long sum) throws InsufficientFundsException {
        if (sum > sender.getBalance()) {
            throw new InsufficientFundsException("Недостаточно средств.", sender);
        }
    }

    private Account checkRecipient(String recipientNumber) throws RecipientNotFoundException {
        Account recipient = accountRepository.findAccountByNumber(recipientNumber);
        if (recipient == null) {
            throw new RecipientNotFoundException("Получатель с таким номером не найден");
        }
        return recipient;
    }
}
