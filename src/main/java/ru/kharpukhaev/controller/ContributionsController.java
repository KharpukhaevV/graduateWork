package ru.kharpukhaev.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.entity.ContributionOffer;
import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.entity.enums.Currency;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.ContributionOfferRepository;
import ru.kharpukhaev.repository.ContributionsRepository;
import ru.kharpukhaev.services.TransferService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contribution")
public class ContributionsController {

    private final ContributionOfferRepository contributionOfferRepository;
    private final ContributionsRepository contributionsRepository;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public ContributionsController(ContributionOfferRepository contributionOfferRepository,
                                   ContributionsRepository contributionsRepository,
                                   ClientRepository clientRepository,
                                   AccountRepository accountRepository, TransferService transferService) {
        this.contributionOfferRepository = contributionOfferRepository;
        this.contributionsRepository = contributionsRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.transferService = transferService;
    }

    @GetMapping
    public String contributions(Principal principal, Model model, @ModelAttribute("contribution") Contribution contribution1) {
        Iterable<ContributionOffer> all = contributionOfferRepository.findAll();
        for (ContributionOffer a : all) {
            a.setTerm(LocalDate.now().plusMonths(a.getMinTerm()));
        }
        Client client =clientRepository.findByUsername(principal.getName());
        List<Account> accounts = client.getAccounts().stream().filter(p -> !p.getType().equals(AccountType.SAVINGS_ACCOUNT)).toList();
        model.addAttribute("accounts", accounts);
        model.addAttribute("client", client);
        model.addAttribute("currency", new Currency[] {Currency.EUR, Currency.USD});
        model.addAttribute("all", all);
        model.addAttribute("contributions", contributionsRepository.findAll());
        return "contributions";
    }

    @PostMapping("/accept")
    public String accept(@ModelAttribute("contribution") Contribution contribution, BindingResult bindingResult, @RequestParam Client client) {
        if (bindingResult.hasErrors()) {
            return "contributions";
        }
        Account account = new Account(AccountType.SAVINGS_ACCOUNT, client, contribution.getCurrency());
        contribution.setAccount(account);
        contribution.setStartDate(LocalDate.now().toString());
        accountRepository.save(account);
        contributionsRepository.save(contribution);
        return "redirect:/contribution";
    }

    @PostMapping("/takeOf")
    public String accountTakeOf(@RequestParam Account accountSender, @RequestParam Long sum, @RequestParam String accountRecipient) {
        if (accountSender.getContribution().getTakeOf()) {
            transferService.transferBetweenTheir(accountSender.getNumber(), accountRecipient, sum);
            return "redirect:/contribution";
        }
        return "redirect:/contribution";
    }

    @PostMapping("/takeUp")
    public String accountTakeUf(@RequestParam String accountSender, @RequestParam Long sum, @RequestParam Account accountRecipient) {
        if (accountRecipient.getContribution().getTakeUp()) {
            transferService.transferBetweenTheir(accountSender, accountRecipient.getNumber(), sum);
            return "redirect:/contribution";
        }
        return "redirect:/contribution";
    }

    @GetMapping("/close")
    public String close(@RequestParam Contribution contribution) {
        if (contribution.getCanBeClosed()) {
            contribution.getAccount().setType(AccountType.CHECKING_ACCOUNT);
            contributionsRepository.delete(contribution);
            return "redirect:/contribution";
        }
        return "redirect:/contribution";
    }
}