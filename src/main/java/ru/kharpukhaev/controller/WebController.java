package ru.kharpukhaev.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.exceptions.InsufficientFunds;
import ru.kharpukhaev.exceptions.RecipientNotFound;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.TransferRepository;
import ru.kharpukhaev.services.Transfer;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final ClientRepository clientRepository;
    private final TransferRepository transferRepository;
    private final Transfer transfer;

    @Autowired
    public WebController(ClientRepository clientRepository, TransferRepository transferRepository, Transfer transfer) {
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.transfer = transfer;
    }

    @GetMapping
    public String index(@ModelAttribute("client") Client client) {
        return "auth";
    }

    @PostMapping("/log")
    public String auth(@ModelAttribute("client") @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth";
        }
        Client sqlClient = clientRepository.findByLogin(client.getLogin());
        if (sqlClient != null) {
            if (sqlClient.getPassword().equals(client.getPassword())) {
                return "redirect:/" + client.getLogin();
            } else {
                return "auth";
            }
        }
        return "auth";
    }

    @GetMapping("/{login}")
    public String profile(@PathVariable("login") String login, Model model) {
        Client client = clientRepository.findByLogin(login);
        List<TransferEntity> transfers = transferRepository.findAllBySenderId(client.getId());
        Collections.reverse(transfers);
        model.addAttribute("client", client);
        model.addAttribute("transfers", transfers);
        return "profile";
    }

    @PostMapping("/account_transfer")
    public String accountTransfer(@RequestParam Client client, Model model) {
        model.addAttribute("client", client);
        return "index";
    }

    @GetMapping("/{login}/card_transfer")
    public String cardTransfer(@ModelAttribute("transferEntity") TransferEntity transferEntity, @PathVariable("login") String login, Model model) {
        model.addAttribute("client", clientRepository.findByLogin(login));
        model.addAttribute("errors", new ArrayList<>());
        return "card_transfer";
    }

    @PostMapping("/{login}/transfer")
    public String transfer(@ModelAttribute("transferEntity") @Valid TransferEntity transferEntity, BindingResult bindingResult, @PathVariable("login") String login) throws InsufficientFunds {
        Client sender = clientRepository.findByLogin(login);
        if (bindingResult.hasErrors()) {
            return "redirect:/" + login + "/card_transfer";
        }
        transfer.doTransfer(sender, transferEntity.getAccountNumber(), transferEntity.getTransferSum());
        return "redirect:/" + login;
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
