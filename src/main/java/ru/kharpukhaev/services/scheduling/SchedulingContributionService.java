package ru.kharpukhaev.services.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ContributionsRepository;

import java.time.LocalDate;
import java.util.List;


@Service
public class SchedulingContributionService {

    private final ContributionsRepository contributionsRepository;
    private final AccountRepository accountRepository;

    public SchedulingContributionService(ContributionsRepository contributionsRepository,
                                         AccountRepository accountRepository) {
        this.contributionsRepository = contributionsRepository;
        this.accountRepository = accountRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void paymentCalc() {
        Iterable<Contribution> contributions = contributionsRepository.findAll();
        for (Contribution contribution : contributions) {
            LocalDate date = LocalDate.parse(contribution.getStartDate());
            Account account = contribution.getAccount();
            if (date.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                account.setBalance((long) (account.getBalance() + (contribution.getSum() * contribution.getStake() / 100)));
                accountRepository.save(account);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void depositExtension() {
        List<Contribution> contributions = contributionsRepository.findAllByExpirationDate(LocalDate.now().toString());
        for (Contribution contribution : contributions) {
            contribution.setExpirationDate(LocalDate.now().plusMonths(3).toString());
            contributionsRepository.save(contribution);
        }
    }
}
