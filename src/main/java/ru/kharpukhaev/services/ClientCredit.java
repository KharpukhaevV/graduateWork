package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.Currency;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.CreditBidRepository;

@Service
public class ClientCredit {

    private final CreditBidRepository creditBidRepository;

    private final CardRepository cardRepository;

    public ClientCredit(CreditBidRepository creditBidRepository, CardRepository cardRepository) {
        this.creditBidRepository = creditBidRepository;
        this.cardRepository = cardRepository;
    }

    public void creditAccept(CreditBid creditBid) {
        creditBid.setStatus(CreditBidStatus.ACTIVE);
        Card card = new Card(CardType.CREDIT, creditBid.getClient());
        if (creditBid.getCurrency().equals(Currency.EUR)) {
            creditBid.setLimitCard((creditBid.getLimitCard() * 80));
            card.getAccount().setBalance(creditBid.getLimitCard() - (creditBid.getLimitCard() / 10));
        } else {
            if (creditBid.getCurrency().equals(Currency.USD)) {
                creditBid.setLimitCard((creditBid.getLimitCard() * 70));
                card.getAccount().setBalance(creditBid.getLimitCard() - (creditBid.getLimitCard() / 10));
            } else {
                card.getAccount().setBalance(creditBid.getLimitCard());
            }
        }
        card.setExpirationDate(creditBid.getExpirationDate());
        creditBid.setCard(card);
        cardRepository.save(card);
        creditBidRepository.save(creditBid);
    }

    public void creditDecline(CreditBid creditBid) {
        creditBid.setStatus(CreditBidStatus.REJECTED);
        creditBidRepository.save(creditBid);
    }
}
