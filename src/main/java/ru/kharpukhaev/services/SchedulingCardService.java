package ru.kharpukhaev.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.CreditRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingCardService {

    private final CardRepository cardRepository;

    private final CreditRepository creditRepository;

    private final ClientRepository clientRepository;

    public SchedulingCardService(CardRepository cardRepository,
                                 CreditRepository creditRepository,
                                 ClientRepository clientRepository) {
        this.cardRepository = cardRepository;
        this.creditRepository = creditRepository;
        this.clientRepository = clientRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeCards() {
        Iterable<Card> cards = cardRepository.findAll();
        for (Card card : cards) {
            if (card.getExpirationDate().isBefore(LocalDate.now())) {
                if (card.getType().equals(CardType.CREDIT)) {
                    List<CreditEntity> overdueCredits = creditRepository.findAllByCardAndCreditStatus(card, CreditStatus.OVERDUE);
                    if (overdueCredits.isEmpty()) {
                        Client client = card.getClient();
                        client.getCards().remove(card);
                        clientRepository.save(client);
                        cardRepository.delete(card);
                    }
                }
            }
        }
    }
}
