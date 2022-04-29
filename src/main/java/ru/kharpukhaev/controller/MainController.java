package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.Role;
import ru.kharpukhaev.repository.AccountRepository;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.ClientRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@Controller
public class MainController {

    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private Client client;

    public MainController(ClientRepository clientRepository,
                          CardRepository cardRepository,
                          AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
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


    /*
    TODO
    Почистить контролеры
    Валидация форм оператора
    Исключения
     */

}
