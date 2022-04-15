package ru.kharpukhaev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.TransferRepository;
import ru.kharpukhaev.service.Commissions;
import ru.kharpukhaev.service.Transfer;

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
    public String index(Model model) {
        return "auth";
    }

    @PostMapping
    public String auth(@RequestParam String login, @RequestParam String password, Model model) {
        Client client = clientRepository.findByLogin(login);
        if (client.getPassword().equals(password)) {
            return "redirect:/" + login;
        } else  {
            return "redirect:/";
        }
    }

    @GetMapping("/{login}")
    public String profile(@PathVariable("login") String login, Model model) {
        Client client = clientRepository.findByLogin(login);
        model.addAttribute("client", client);
        boolean isCard = false;
        boolean isAccount = false;
        model.addAttribute("isCard", isCard);
        model.addAttribute("isAccount", isAccount);
        model.addAttribute("transfers", transferRepository.findAllBySenderId(client.getId()));
        return "profile";
    }

    @PostMapping("/account_transfer")
    public String accountTransfer(@RequestParam long clientId, Model model) {
        model.addAttribute("clientId", clientId);
        return "index";
    }

    @PostMapping("/card_transfer")
    public String cardTransfer(@RequestParam long clientId, Model model) {
        model.addAttribute("clientId", clientId);
        return "card_transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String recipientId, @RequestParam long sum, @RequestParam long senderId) {
        Client sender = clientRepository.findFirstById(senderId);
        Client recipient = clientRepository.findByCardNumber(recipientId);
        if (recipient != null) {
            Map<String, Client> map = null;
            try {
                map = transfer.doTransfer(sender, recipient, sum);
                sender = map.get("sender");
                recipient = map.get("recipient");
                transferRepository.save(new TransferEntity(sender, recipientId, sum));
                clientRepository.save(recipient);
                clientRepository.save(sender);
            } catch (Exception e) {
                return "404";
            }
        }
        return "redirect:/" + sender.getLogin();
    }

}
