package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ContributionsRepository;

import java.time.LocalDate;

@Service
public class ContributionService {

    private final AccountRepository accountRepository;
    private final ContributionsRepository contributionsRepository;

    private final CurrencyConvertService currencyConvertService;

    public ContributionService(AccountRepository accountRepository,
                               ContributionsRepository contributionsRepository,
                               CurrencyConvertService currencyConvertService) {
        this.accountRepository = accountRepository;
        this.contributionsRepository = contributionsRepository;
        this.currencyConvertService = currencyConvertService;
    }

    @Transactional
    public void openContribution(Contribution contribution, String accountNum) {
        Account accountSender = accountRepository.findAccountByNumber(accountNum);
        Account accountContribution = new Account(AccountType.SAVINGS_ACCOUNT, contribution.getClient(), contribution.getCurrency());

        accountContribution.setCurrency(contribution.getCurrency());

        Long sumCurrency = currencyConvertService.checkCurrencyAndConvert(contribution.getCurrency(), accountSender.getCurrency(), contribution.getSum());
        accountSender.setBalance(accountSender.getBalance() - sumCurrency);

        accountContribution.setBalance(contribution.getSum());

        contribution.setAccount(accountContribution);
        contribution.setStartDate(LocalDate.now().toString());

        accountRepository.save(accountContribution);
        accountRepository.save(accountSender);
        contributionsRepository.save(contribution);
    }
}
