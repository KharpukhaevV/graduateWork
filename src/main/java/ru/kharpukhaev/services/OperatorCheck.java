package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.CreditBidRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.time.LocalDate;

@Service
public class OperatorCheck {

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final CreditBidRepository creditBidRepository;

    public OperatorCheck(TransferRepository transferRepository, AccountRepository accountRepository, CreditBidRepository creditBidRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.creditBidRepository = creditBidRepository;
    }

    public void paymentAccept(TransferEntity transferEntity) {
        String numberAccountRecipient = transferEntity.getAccountRecipient();
        Account accountRecipient = accountRepository.findAccountByNumber(numberAccountRecipient);
        accountRecipient.setBalance(accountRecipient.getBalance() + transferEntity.getTransferSum());
        transferEntity.setStatus(TransferStatus.SUCCESS);
        transferRepository.save(transferEntity);
        accountRepository.save(accountRecipient);
    }

    public void paymentDecline(TransferEntity transferEntity) {
        String numberAccountSender = transferEntity.getAccountRecipient();
        Account accountSender = accountRepository.findAccountByNumber(numberAccountSender);
        accountSender.setBalance(accountSender.getBalance() + transferEntity.getTransferSum());
        transferEntity.setStatus(TransferStatus.BLOCKED);
        transferRepository.save(transferEntity);
        accountRepository.save(accountSender);
    }

    public void makeCreditOffer(CreditBid creditBid, long limit, double percent, LocalDate date) {
        creditBid.setLimitCard(limit);
        creditBid.setPercent(percent);
        creditBid.setStartDate(LocalDate.now());
        creditBid.setExpirationDate(date);
        creditBid.setStatus(CreditBidStatus.OFFERED);
        creditBidRepository.save(creditBid);
    }

    public void decline(CreditBid creditBid) {
        creditBid.setStatus(CreditBidStatus.REJECTED);
        creditBidRepository.save(creditBid);
    }
}
