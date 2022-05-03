package ru.kharpukhaev.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import ru.kharpukhaev.services.ContributionService;
import ru.kharpukhaev.services.CurrencyConvertService;
import ru.kharpukhaev.services.TransferService;
import ru.kharpukhaev.services.TransferValidationService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/contribution")
public class ContributionsController {

    private final ContributionOfferRepository contributionOfferRepository;
    private final ContributionsRepository contributionsRepository;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransferService transferService;
    private final TransferValidationService transferValidationService;
    private final ContributionService contributionService;
    private final CurrencyConvertService currencyConvertService;
    private Client client;

    public ContributionsController(ContributionOfferRepository contributionOfferRepository,
                                   ContributionsRepository contributionsRepository,
                                   ClientRepository clientRepository,
                                   AccountRepository accountRepository,
                                   TransferService transferService,
                                   TransferValidationService transferValidationService,
                                   ContributionService contributionService,
                                   CurrencyConvertService currencyConvertService) {
        this.contributionOfferRepository = contributionOfferRepository;
        this.contributionsRepository = contributionsRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.transferService = transferService;
        this.transferValidationService = transferValidationService;
        this.contributionService = contributionService;
        this.currencyConvertService = currencyConvertService;
    }

    @GetMapping
    public String contributions(Principal principal, Model model, @ModelAttribute("contribution") Contribution contribution1) {
        Iterable<ContributionOffer> all = contributionOfferRepository.findAll();
        for (ContributionOffer a : all) {
            a.setTerm(LocalDate.now().plusMonths(a.getMinTerm()));
        }
        client = clientRepository.findByUsername(principal.getName());
        model.addAttribute("accounts", client.getCheckingAccounts());
        model.addAttribute("client", client);
        model.addAttribute("currency", new Currency[] {Currency.EUR, Currency.USD});
        model.addAttribute("all", all);
        model.addAttribute("contributions", contributionsRepository.findAllByClient(client));
        return "contributions";
    }

    @PostMapping("/accept")
    public String accept(@ModelAttribute("contribution") @Valid Contribution contribution, BindingResult bindingResult, @RequestParam String accountNum, Model model) {
        Account accountByNumber = accountRepository.findAccountByNumber(accountNum);
        Long sumCurrency = currencyConvertService.checkCurrencyAndConvert(contribution.getCurrency(), accountByNumber.getCurrency(), contribution.getSum());
        String err = transferValidationService.validateTransferSum(accountNum, sumCurrency);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            Iterable<ContributionOffer> all = contributionOfferRepository.findAll();
            for (ContributionOffer a : all) {
                a.setTerm(LocalDate.now().plusMonths(a.getMinTerm()));
            }
            model.addAttribute("accounts", client.getCheckingAccounts());
            model.addAttribute("client", client);
            model.addAttribute("currency", new Currency[] {Currency.EUR, Currency.USD});
            model.addAttribute("all", all);
            model.addAttribute("contributions", contributionsRepository.findAll());
            return "contributions";
        }
        contributionService.openContribution(contribution, accountNum);
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
