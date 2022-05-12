package ru.kharpukhaev.services.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ContributionsRepository;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;


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
        List<Contribution> contributions = (List<Contribution>) contributionsRepository.findAll();

        LocalDate today = LocalDate.now();

        List<Contribution> filteredContributions = contributions.stream()
                .filter(p -> LocalDate.parse(p.getStartDate()).getDayOfMonth() == today.getDayOfMonth() |
                        (LocalDate.parse(p.getStartDate()).getDayOfMonth() > today.with(lastDayOfMonth()).getDayOfMonth() &
                                today.getDayOfMonth() == today.with(lastDayOfMonth()).getDayOfMonth()))
                .toList();

        for (Contribution contribution : filteredContributions) {
            Account account = contribution.getAccount();
            account.setBalance((long) (account.getBalance() + (account.getBalance() * contribution.getStake() / 100)));
            accountRepository.save(account);
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
