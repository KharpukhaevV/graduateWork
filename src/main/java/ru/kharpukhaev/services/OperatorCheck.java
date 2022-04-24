package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.CreditBidRepository;
import ru.kharpukhaev.repository.TransferRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OperatorCheck {

    private final TransferRepository transferRepository;

    private final CardRepository cardRepository;

    private final CreditBidRepository creditBidRepository;

    public OperatorCheck(TransferRepository transferRepository, CardRepository cardRepository, CreditBidRepository creditBidRepository) {
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
        this.creditBidRepository = creditBidRepository;
    }

    public void paymentAccept(TransferEntity transferEntity) {
//        Card cardRecipient = transferEntity.getCardRecipient();
//        cardRecipient.setBalance(cardRecipient.getBalance() + transferEntity.getTransferSum());
//        transferEntity.setStatus(TransferStatus.SUCCESS);
//        transferRepository.save(transferEntity);
//        cardRepository.save(cardRecipient);
    }

    public void paymentDecline(TransferEntity transferEntity) {
//        Card cardSender = transferEntity.getCardSender();
//        cardSender.setBalance(cardSender.getBalance() + transferEntity.getTransferSum());
//        transferEntity.setStatus(TransferStatus.BLOCKED);
//        transferRepository.save(transferEntity);
//        cardRepository.save(cardSender);
    }

    public void makeCreditOffer(CreditBid creditBid, long limit, double percent, String date) {
        creditBid.setLimitCard(limit);
        creditBid.setPercent(percent / 100);
        creditBid.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        creditBid.setExpirationDate(date);
        creditBid.setStatus(CreditStatus.OFFERED);
        creditBidRepository.save(creditBid);
    }
}
