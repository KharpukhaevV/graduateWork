package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.ClientCreditRequest;
import ru.kharpukhaev.entity.CreditOfferEntity;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ClientCreditRequestRepository;
import ru.kharpukhaev.repository.CreditOfferRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.time.LocalDate;

@Service
public class OperatorCheck {

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final CreditOfferRepository creditOfferRepository;

    private final ClientCreditRequestRepository clientCreditRequestRepository;

    public OperatorCheck(TransferRepository transferRepository,
                         AccountRepository accountRepository,
                         CreditOfferRepository creditOfferRepository,
                         ClientCreditRequestRepository clientCreditRequestRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.creditOfferRepository = creditOfferRepository;
        this.clientCreditRequestRepository = clientCreditRequestRepository;
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

    public void makeCreditOffer(CreditOfferEntity creditOfferEntity, ClientCreditRequest request) {
        creditOfferEntity.setStartDate(LocalDate.now().toString());
        creditOfferEntity.setStatus(CreditBidStatus.OFFERED);
        clientCreditRequestRepository.delete(request);
        creditOfferRepository.save(creditOfferEntity);
    }

    public void decline(ClientCreditRequest request) {
        clientCreditRequestRepository.delete(request);
    }
}
