package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Credit;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.CreditRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OperatorCheck {

    private final TransferRepository transferRepository;

    private final CardRepository cardRepository;

    private final CreditRepository creditRepository;

    public OperatorCheck(TransferRepository transferRepository, CardRepository cardRepository, CreditRepository creditRepository) {
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
        this.creditRepository = creditRepository;
    }

    public void paymentAccept(TransferEntity transferEntity) {
        Card cardRecipient = transferEntity.getCardRecipient();
        cardRecipient.setBalance(cardRecipient.getBalance() + transferEntity.getTransferSum());
        transferEntity.setStatus(TransferStatus.SUCCESS);
        transferRepository.save(transferEntity);
        cardRepository.save(cardRecipient);
    }

    public void paymentDecline(TransferEntity transferEntity) {
        Card cardSender = transferEntity.getCardSender();
        cardSender.setBalance(cardSender.getBalance() + transferEntity.getTransferSum());
        transferEntity.setStatus(TransferStatus.BLOCKED);
        transferRepository.save(transferEntity);
        cardRepository.save(cardSender);
    }

    public void makeCreditOffer(Credit credit, long limit, double percent, String date) {
        credit.setLimitCard(limit);
        credit.setPercent(percent / 100);
        credit.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        credit.setExpirationDate(date);
        credit.setStatus(CreditStatus.OFFERED);
        creditRepository.save(credit);
    }
}
