package ru.kharpukhaev.services.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.repository.CreditRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingCreditService {

    private final CreditRepository creditRepository;

    public SchedulingCreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkStatus() {
        List<CreditEntity> creditsList = creditRepository.findAllByCreditStatus(CreditStatus.ACTIVE);
        for (CreditEntity credit : creditsList) {
            if (credit.getExpirationDate().isBefore(LocalDate.now())) {
                if (credit.getCard().getAccount().getBalance() < credit.getCard().getCreditOffer().getLimitCard()) {
                    credit.setCreditStatus(CreditStatus.OVERDUE);
                    creditRepository.save(credit);
                } else {
                    credit.setCreditStatus(CreditStatus.CLOSED);
                    creditRepository.save(credit);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkPenalties() {
        List<CreditEntity> creditsList = creditRepository.findAllByCreditStatus(CreditStatus.OVERDUE);
        for (CreditEntity credit : creditsList) {
            if (credit.getCard().getAccount().getBalance() < credit.getCard().getCreditOffer().getLimitCard() + (credit.getSum() / 100 * credit.getPenalties())) {
                if (credit.getPenalties() < 40) {
                    credit.setPenalties(credit.getPenalties() + 1);
                    credit.setSum(credit.getSum() + (credit.getSum() / 100 * credit.getPenalties()));
                    creditRepository.save(credit);
                }
            } else {
                credit.setCreditStatus(CreditStatus.CLOSED);
                creditRepository.save(credit);
            }
        }
    }


}