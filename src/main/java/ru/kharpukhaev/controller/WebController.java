package ru.kharpukhaev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CardType;
import ru.kharpukhaev.entity.enums.Role;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
import ru.kharpukhaev.repository.CardRepository;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.TransferRepository;
import ru.kharpukhaev.services.Transfer;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class WebController {

    private final ClientRepository clientRepository;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final Transfer transfer;

    @Autowired
    public WebController(ClientRepository clientRepository, TransferRepository transferRepository, CardRepository cardRepository, Transfer transfer) {
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.cardRepository = cardRepository;
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
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        Client clientFromDB = clientRepository.findByUsername(client.getUsername());
        if (clientFromDB != null) {
            return "registration";
        }
        client.setActive(true);
        client.setRoles(Collections.singleton(Role.USER));
        clientRepository.save(client);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        List<TransferEntity> transfers = transferRepository.findAllBySenderId(client.getId());
        Collections.reverse(transfers);
        model.addAttribute("client", client);
        model.addAttribute("transfers", transfers);
        return "profile";
    }

    @GetMapping("/add_card")
    public String addCard(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        Card card = new Card();
        card.setType(CardType.DEBIT);
        card.setClient(client);
        card.setNumber("446644" + (1000000000 + (long) (Math.random() * 9999999999L)));
        cardRepository.save(card);
        return "redirect:/profile";
    }

    @GetMapping("/transfer")
    public String transfers() {
        return "transfer";
    }

    @GetMapping("/transfer/to_client")
    public String cardTransfer(@ModelAttribute("transferEntity") TransferEntity transferEntity, Model model, Principal principal) {
        model.addAttribute("client", clientRepository.findByUsername(principal.getName()));
        return "transfer_form";
    }

    @PostMapping("/transfer/to_client")
    public String transfer(@ModelAttribute("transferEntity") @Valid TransferEntity transferEntity, BindingResult bindingResult, Principal principal) throws InsufficientFunds {
//        if (bindingResult.hasErrors()) {
//            return "transfer_form";
//        }
        System.out.println(transferEntity.getSender());
        transfer.doTransferToOurBank(transferEntity.getSender(), transferEntity.getAccountNumber(), transferEntity.getTransferSum());
        return "profile";
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

            /* TODO
                Обработка исключений
                новые сущности карт и счетов
                переводы на картах и счетах
                переводы между своими счетами
                авторизация и админка
             */
}
