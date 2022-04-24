package ru.kharpukhaev.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CreditRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingService {

    private final CreditRepository creditRepository;

    public SchedulingService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Scheduled(cron = "0 29 17 * * *")
    public void checkStatus() {
        List<CreditEntity> creditsList = creditRepository.findAllByCreditStatus(CreditStatus.ACTIVE);
        for (CreditEntity credit : creditsList) {
            if (credit.getExpirationDate().isBefore(LocalDate.now())) {
                if (credit.getCard().getAccount().getBalance() < credit.getCard().getCreditBid().getLimitCard()) {
                    credit.setCreditStatus(CreditStatus.OVERDUE);
                } else {
                    credit.setCreditStatus(CreditStatus.CLOSED);
                }
            }
            creditRepository.save(credit);
        }
    }

    @Scheduled(cron = "0 29 17 * * *")
    public void checkPenalties() {
        List<CreditEntity> creditsList = creditRepository.findAllByCreditStatus(CreditStatus.OVERDUE);
        for (CreditEntity credit : creditsList) {
            if (credit.getCard().getAccount().getBalance() < credit.getCard().getCreditBid().getLimitCard() + (credit.getSum() / 100 * credit.getPenalties())) {
                if (credit.getPenalties() < 40) {
                    credit.setPenalties(credit.getPenalties() + 1);
                }
            } else {
                credit.setCreditStatus(CreditStatus.CLOSED);
            }
            creditRepository.save(credit);
        }
    }


}
