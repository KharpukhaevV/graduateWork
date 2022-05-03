package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CreditRepository;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public void addCredit(long sum, Card card) {
        boolean bid = true;
        for (CreditEntity credit : card.getCredits()) {
            if (credit.getCreditStatus() == CreditStatus.OVERDUE) {
                bid = false;
                break;
            }
        }
        if (bid) {
            creditRepository.save(new CreditEntity(sum, card));
        }
    }
}
