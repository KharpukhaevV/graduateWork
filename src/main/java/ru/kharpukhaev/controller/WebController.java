package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.enums.*;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
import ru.kharpukhaev.repository.*;
import ru.kharpukhaev.services.ClientCredit;
import ru.kharpukhaev.services.Transfer;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@Controller
public class WebController {

    private final ClientRepository clientRepository;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final CreditBidRepository creditBidRepository;

    private final AccountRepository accountRepository;

    private final ClientCredit clientCredit;
    private final Transfer transfer;

    private Client client;

    public WebController(ClientRepository clientRepository,
                         TransferRepository transferRepository,
                         CardRepository cardRepository,
                         CreditBidRepository creditBidRepository,
                         AccountRepository accountRepository, ClientCredit clientCredit, Transfer transfer) {
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
        this.creditBidRepository = creditBidRepository;
        this.accountRepository = accountRepository;
        this.clientCredit = clientCredit;
        this.transfer = transfer;
    }

    @GetMapping
    public String index() {
        return "redirect:/profile";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("client") Client client) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("client") @Valid Client client, BindingResult bindingResult) {
        Client clientFromDB = clientRepository.findByUsername(client.getUsername());
        if (bindingResult.hasErrors() | clientFromDB != null) {
            return "registration";
        }
        client.setActive(true);
        client.setRoles(Collections.singleton(Role.USER));
        clientRepository.save(client);
        return "redirect:/login";
    }

    //    @AuthenticationPrincipal Client client
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        client = clientRepository.findByUsername(principal.getName());
        model.addAttribute("client", client);
        model.addAttribute("admin", Role.ADMIN);
        model.addAttribute("moder", Role.MODERATOR);
        return "profile";
    }

    @GetMapping("/add_card")
    public String addCard() {
        Card card = new Card(CardType.DEBIT, client);
        cardRepository.save(card);
        return "redirect:/profile";
    }

    @GetMapping("/add_account")
    public String addAccount() {
        Account account = new Account(AccountType.CHECKING_ACCOUNT, client);
        Account account1 = new Account(AccountType.CURRENCY_ACCOUNT, client);
        Account account2 = new Account(AccountType.SAVINGS_ACCOUNT, client);
        accountRepository.save(account);
        accountRepository.save(account1);
        accountRepository.save(account2);
        return "redirect:/profile";
    }

    @GetMapping("/transfer/do_transfer")
    public String transferToClient(Model model) {
        model.addAttribute("client", client);
        model.addAttribute("admin", Role.ADMIN);
        model.addAttribute("moder", Role.MODERATOR);
        return "transfer_form";
    }

    @PostMapping("/transfer/do_transfer")
    public String doTransferToClient(@RequestParam String sender, @RequestParam String recipient, @RequestParam long sum) throws InsufficientFunds {
        transfer.doTransfer(sender, recipient, sum);
        return "redirect:/profile";
    }

    @GetMapping("/transfer/between_their")
    public String transferBetweenTheir(Model model) {
        model.addAttribute("client", client);
        model.addAttribute("admin", Role.ADMIN);
        model.addAttribute("moder", Role.MODERATOR);
        return "transfer_form";
    }

    @PostMapping("/transfer/between_their")
    public String doTransferBetweenTheir(@RequestParam Card sender, @RequestParam Card recipient, @RequestParam long sum) throws InsufficientFunds {
        transfer.transferBetweenTheir(sender, recipient, sum);
        return "redirect:/profile";
    }

    @GetMapping("/credit")
    public String credit(Model model) {
        model.addAttribute("client", client);
        model.addAttribute("admin", Role.ADMIN);
        model.addAttribute("moder", Role.MODERATOR);
        model.addAttribute("status", CreditStatus.OFFERED);
        return "credit";
    }

    @PostMapping("/credit")
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

    @ExceptionHandler(InsufficientFunds.class)
    public String handleInsufficientFunds(InsufficientFunds e, Model model) {
        model.addAttribute("errors", e.getMessage());
        return "123123";
//        return "/" + e.getSender().getLogin() + "/card_transfer";
    }

    @ExceptionHandler(RecipientNotFound.class)
    public String handleRecipientNotFound(RecipientNotFound e, Model model) {
        return "";
    }


    /*
    TODO
    Почистить котролеры
    Реализовать переводы карта счет и тд
    Реализовать OperatorCheck
     */

}
