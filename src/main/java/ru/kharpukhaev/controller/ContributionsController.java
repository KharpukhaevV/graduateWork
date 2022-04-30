package ru.kharpukhaev.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Contribution;
import ru.kharpukhaev.entity.ContributionOffer;
import ru.kharpukhaev.entity.enums.Currency;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.ContributionOfferRepository;
import ru.kharpukhaev.repository.ContributionsRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/contribution")
public class ContributionsController {

    private final ContributionOfferRepository contributionOfferRepository;
    private final ContributionsRepository contributionsRepository;
    private final ClientRepository clientRepository;

    public ContributionsController(ContributionOfferRepository contributionOfferRepository, ContributionsRepository contributionsRepository, ClientRepository clientRepository) {
        this.contributionOfferRepository = contributionOfferRepository;
        this.contributionsRepository = contributionsRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String contributions(@ModelAttribute("contribution") Contribution contribution, Principal principal, Model model) {
//        ContributionOffer contribution1 = new ContributionOffer("Портфель сбережений", 10.0, 3, false, false, false);
//        ContributionOffer contribution2 = new ContributionOffer("Валютный сохрани", 10.0, 12, false, true, true);
//        ContributionOffer contribution3 = new ContributionOffer("Валютный управляй", 10.0, 6, true, true, true);
//        ContributionOffer contribution4 = new ContributionOffer("Социальный", 10.0, 6, true, true, false);
//        contributionOfferRepository.save(contribution1);
//        contributionOfferRepository.save(contribution2);
//        contributionOfferRepository.save(contribution3);
//        contributionOfferRepository.save(contribution4);
        Iterable<ContributionOffer> all = contributionOfferRepository.findAll();
        for (ContributionOffer a : all) {
            a.setTerm(LocalDate.now().plusMonths(a.getMinTerm()));
        }
        model.addAttribute("client", clientRepository.findByUsername(principal.getName()));
        model.addAttribute("currency", new Currency[] {Currency.EUR, Currency.USD});
        model.addAttribute("all", all);
        model.addAttribute("contributions", contributionsRepository.findAll());
        return "contributions";
    }

    @PostMapping("/accept")
    public String accept(@ModelAttribute("contribution") @Valid Contribution contribution, BindingResult bindingResult,
                         @RequestParam String term,
                         @RequestParam Currency currency) {
//        if (bindingResult.hasErrors()) {
//            return "contributions";
//        }
        contribution.setTerm(LocalDate.parse(term));
        contribution.setCurrency(currency);
        contributionsRepository.save(contribution);
        return "redirect:/contribution";
    }
}
