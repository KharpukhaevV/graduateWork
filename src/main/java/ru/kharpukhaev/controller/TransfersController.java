package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.services.transfer.TransferService;
import ru.kharpukhaev.services.validation.ValidationService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/transfer")
public class TransfersController {

    private final ValidationService validationService;
    private final TransferService transferService;
    private final ClientRepository clientRepository;
    private Client client;

    public TransfersController(ValidationService validationService,
                               TransferService transferService,
                               ClientRepository clientRepository) {
        this.validationService = validationService;
        this.transferService = transferService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/do_transfer")
    public String transferToClient(@ModelAttribute("transfer") TransferEntity transferEntity, Principal principal, Model model) {
        client = clientRepository.findByUsername(principal.getName());
        model.addAttribute("client", client);
        return "transfer_form";
    }

    @PostMapping("/do_transfer")
    public String doTransferToClient(@ModelAttribute("transfer") @Valid TransferEntity transferEntity, BindingResult bindingResult, Model model) {
        validationService.validateTransferSum(transferEntity.getAccountSender(), transferEntity.getTransferSum(), bindingResult);
        validationService.validateAccount(transferEntity.getAccountRecipient(), bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", client);
            return "transfer_form";
        }
        transferService.doTransfer(transferEntity.getAccountSender(), transferEntity.getAccountRecipient(), transferEntity.getTransferSum());
        return "redirect:/profile";
    }

    @GetMapping("/between_their")
    public String transferBetweenTheir(@ModelAttribute("transfer") TransferEntity transferEntity, Principal principal, Model model) {
        client = clientRepository.findByUsername(principal.getName());
        model.addAttribute("accounts", client.getCheckingAccounts());
        model.addAttribute("client", client);
        return "transfer_form";
    }

    @PostMapping("/between_their")
    public String doTransferBetweenTheir(@ModelAttribute("transfer") @Valid TransferEntity transferEntity, BindingResult bindingResult, Model model) {
        validationService.validateTransferSum(transferEntity.getAccountSender(), transferEntity.getTransferSum(), bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", client);
            return "transfer_form";
        }
        transferService.transferBetweenTheir(transferEntity.getAccountSender(), transferEntity.getAccountRecipient(), transferEntity.getTransferSum());
        return "redirect:/profile";
    }
}
