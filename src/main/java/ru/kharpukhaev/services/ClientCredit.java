package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.CreditBidRepository;

@Service
public class ClientCredit {

    private final CreditBidRepository creditBidRepository;

    private final ClientRepository clientRepository;

    private final CardRepository cardRepository;

    public ClientCredit(CreditBidRepository creditBidRepository, ClientRepository clientRepository, CardRepository cardRepository) {
        this.creditBidRepository = creditBidRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
    }

    public void creditAccept(CreditBid creditBid) {
        creditBid.setStatus(CreditStatus.ACTIVE);
        Card card = new Card(CardType.CREDIT, creditBid.getClient());
        card.getAccount().setBalance(creditBid.getLimitCard());
        cardRepository.save(card);
        creditBidRepository.save(creditBid);
    }

    public void creditDecline(CreditBid creditBid) {
        creditBid.setStatus(CreditStatus.REJECTED);
        creditBidRepository.save(creditBid);
    }
}
