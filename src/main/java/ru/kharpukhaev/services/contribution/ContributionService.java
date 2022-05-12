package ru.kharpukhaev.services.contribution;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ContributionsRepository;
import ru.kharpukhaev.services.credit.CreditService;
import ru.kharpukhaev.services.transfer.CurrencyConvertService;

import java.time.LocalDate;

@Service
public class ContributionService {

    private final AccountRepository accountRepository;
    private final ContributionsRepository contributionsRepository;
    private final CurrencyConvertService currencyConvertService;
    private final CreditService creditService;

    public ContributionService(AccountRepository accountRepository,
                               ContributionsRepository contributionsRepository,
                               CurrencyConvertService currencyConvertService,
                               CreditService creditService) {
        this.accountRepository = accountRepository;
        this.contributionsRepository = contributionsRepository;
        this.currencyConvertService = currencyConvertService;
        this.creditService = creditService;
    }

    @Transactional
    public void openContribution(Contribution contribution, String accountNum) {
        Account accountSender = accountRepository.findAccountByNumber(accountNum);
        Account accountContribution = new Account(AccountType.SAVINGS_ACCOUNT, contribution.getClient(), contribution.getCurrency());

        Long sumCurrency = currencyConvertService.checkCurrencyAndConvert(contribution.getCurrency(), accountSender.getCurrency(), contribution.getSum());

        if (accountSender.getCard() != null) {
            if (accountSender.getCard().getType().equals(CardType.CREDIT)) {
                creditService.addCredit(sumCurrency, accountSender.getCard());
            }
        }

        accountSender.setBalance(accountSender.getBalance() - sumCurrency);
        accountContribution.setBalance(contribution.getSum());

        contribution.setAccount(accountContribution);
        contribution.setStartDate(LocalDate.now().toString());

        accountRepository.save(accountContribution);
        accountRepository.save(accountSender);
        contributionsRepository.save(contribution);
    }
}
