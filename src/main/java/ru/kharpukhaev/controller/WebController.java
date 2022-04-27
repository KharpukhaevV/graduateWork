package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.*;
import ru.kharpukhaev.entity.enums.*;
import ru.kharpukhaev.exceptions.InsufficientFundsException;
import ru.kharpukhaev.exceptions.RecipientNotFoundException;
import ru.kharpukhaev.repository.*;
import ru.kharpukhaev.services.ClientCredit;
import ru.kharpukhaev.services.TransferService;

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

    private final CreditRepository creditRepository;

    private final ClientCredit clientCredit;
    private final TransferService transferService;

    private Client client;

    public WebController(ClientRepository clientRepository,
                         TransferRepository transferRepository,
                         CardRepository cardRepository,
                         CreditBidRepository creditBidRepository,
                         AccountRepository accountRepository,
                         CreditRepository creditRepository,
                         ClientCredit clientCredit,
                         TransferService transferService) {
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
        this.creditBidRepository = creditBidRepository;
        this.accountRepository = accountRepository;
        this.creditRepository = creditRepository;
        this.clientCredit = clientCredit;
        this.transferService = transferService;
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
        model.addAttribute("accounts", client.getAccounts().stream().filter(p -> p.getCard() == null).toList());
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
    public String doTransferToClient(@RequestParam Account sender, @RequestParam String recipient, @RequestParam long sum) throws InsufficientFundsException {
        transferService.doTransfer(sender, recipient, sum);
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
    public String doTransferBetweenTheir(@RequestParam Account sender, @RequestParam Account recipient, @RequestParam long sum) throws InsufficientFundsException {
        transferService.transferBetweenTheir(sender, recipient, sum);
        return "redirect:/profile";
    }

    @GetMapping("/credit")
    public String credit(Model model, Principal principal) {
        client = clientRepository.findByUsername(principal.getName());
        Iterable<CreditEntity> creditEntities = creditRepository.findAll();
        model.addAttribute("credits", creditEntities);
        model.addAttribute("client", client);
        model.addAttribute("admin", Role.ADMIN);
        model.addAttribute("moder", Role.MODERATOR);
        model.addAttribute("status", CreditBidStatus.OFFERED);
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

    @ExceptionHandler(InsufficientFundsException.class)
    public String handleInsufficientFunds(InsufficientFundsException e, Model model) {
        model.addAttribute("errors", e.getMessage());
        return "123123";
//        return "/" + e.getSender().getLogin() + "/card_transfer";
    }

    @ExceptionHandler(RecipientNotFoundException.class)
    public String handleRecipientNotFound(RecipientNotFoundException e, Model model) {
        return "";
    }


    /*
    TODO
    Почистить котролеры
    Транзакции
    Исключения
     */

}
