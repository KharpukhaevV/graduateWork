package ru.kharpukhaev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.CreditRepository;
import ru.kharpukhaev.repository.TransferRepository;

@Component
public class Transfer {
    private final Commissions commissions;
    private final FrodMonitor frodMonitor;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    private final CreditRepository creditRepository;

    @Autowired
    public Transfer(Commissions commissions,
                    FrodMonitor frodMonitor,
                    TransferRepository transferRepository,
                    AccountRepository accountRepository,
                    CreditRepository creditRepository) {
        this.commissions = commissions;
        this.frodMonitor = frodMonitor;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.creditRepository = creditRepository;
    }

    public void doTransfer(Account sender, String recipientNumber, long sum) throws InsufficientFunds {
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
            creditRepository.save(new CreditEntity(sum, accountSender.getCard()));
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
            creditRepository.save(new CreditEntity(sum, sender.getCard()));
        }
        sender.setBalance(sender.getBalance() - sum);
        recipient.setBalance(recipient.getBalance() + sum);
        TransferEntity transferEntity = new TransferEntity(sender, recipient, sum);
        transferEntity.setStatus(TransferStatus.SUCCESS);
        accountRepository.save(recipient);
        accountRepository.save(sender);
        transferRepository.save(transferEntity);
    }

    private void checkSum(Account sender, long sum) throws InsufficientFunds {
        if (sum > sender.getBalance()) {
            throw new InsufficientFunds("Недостаточно средств.", sender);
        }
    }

    private Account checkRecipient(String recipientNumber) throws RecipientNotFound {
        Account recipient = accountRepository.findAccountByNumber(recipientNumber);
        if (recipient == null) {
            throw new RecipientNotFound("Получатель с таким номером не найден");
        }
        return recipient;
    }
}
