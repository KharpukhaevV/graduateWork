package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CreditBidStatus;
import ru.kharpukhaev.entity.enums.Currency;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.CreditBidRepository;
import ru.kharpukhaev.repository.CreditRepository;
import ru.kharpukhaev.services.ClientCredit;

import java.security.Principal;

@Controller
@RequestMapping("/credit")
public class CreditController {

    private final ClientRepository clientRepository;
    private final CreditBidRepository creditBidRepository;
    private final CreditRepository creditRepository;
    private final ClientCredit clientCredit;
    private Client client;

    public CreditController(ClientRepository clientRepository, CreditBidRepository creditBidRepository, CreditRepository creditRepository, ClientCredit clientCredit) {
        this.clientRepository = clientRepository;
        this.creditBidRepository = creditBidRepository;
        this.creditRepository = creditRepository;
        this.clientCredit = clientCredit;
    }

    @GetMapping
    public String credit(Model model, Principal principal) {
        client = clientRepository.findByUsername(principal.getName());
        Iterable<CreditEntity> creditEntities = creditRepository.findAll();
        model.addAttribute("credits", creditEntities);
        model.addAttribute("client", client);
        model.addAttribute("status", CreditBidStatus.OFFERED);
        return "credit";
    }

    @PostMapping
    public String getCredit(@RequestParam Currency currency) {
        CreditBid creditBid = new CreditBid(client, currency);
        creditBidRepository.save(creditBid);
        return "redirect:/credit";
    }

    @PostMapping("/accept")
    public String acceptCredit(@RequestParam CreditBid creditBid) {
        clientCredit.creditAccept(creditBid);
        return "redirect:/credit";
    }

    @PostMapping("/decline")
    public String declineCredit(@RequestParam CreditBid creditBid) {
        clientCredit.creditDecline(creditBid);
        return "redirect:/credit";
    }
}
