package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.CreditOfferEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.Currency;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.CreditOfferRepository;

import java.time.LocalDate;

@Service
public class ClientCredit {

    private final CreditOfferRepository creditOfferRepository;

    private final CardRepository cardRepository;

    public ClientCredit(CreditOfferRepository creditOfferRepository, CardRepository cardRepository) {
        this.creditOfferRepository = creditOfferRepository;
        this.cardRepository = cardRepository;
    }

    public void creditAccept(CreditOfferEntity creditOfferEntity) {
        creditOfferEntity.setStatus(CreditBidStatus.ACTIVE);
        Card card = new Card(CardType.CREDIT, creditOfferEntity.getClient());
        if (creditOfferEntity.getCurrency().equals(Currency.EUR)) {
            creditOfferEntity.setLimitCard((creditOfferEntity.getLimitCard() * 80));
            card.getAccount().setBalance(creditOfferEntity.getLimitCard() - (creditOfferEntity.getLimitCard() / 10));
        } else {
            if (creditOfferEntity.getCurrency().equals(Currency.USD)) {
                creditOfferEntity.setLimitCard((creditOfferEntity.getLimitCard() * 70));
                card.getAccount().setBalance(creditOfferEntity.getLimitCard() - (creditOfferEntity.getLimitCard() / 10));
            } else {
                card.getAccount().setBalance(creditOfferEntity.getLimitCard());
            }
        }
        card.setExpirationDate(LocalDate.parse(creditOfferEntity.getExpirationDate()));
        creditOfferEntity.setCard(card);
        cardRepository.save(card);
        creditOfferRepository.save(creditOfferEntity);
    }

    public void creditDecline(CreditOfferEntity creditOfferEntity) {
        creditOfferEntity.setStatus(CreditBidStatus.REJECTED);
        creditOfferRepository.save(creditOfferEntity);
    }
}
