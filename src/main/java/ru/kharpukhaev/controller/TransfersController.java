package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.AccountType;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.services.TransferService;
import ru.kharpukhaev.services.TransferValidationService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransfersController {

    private final TransferValidationService transferValidationService;
    private final TransferService transferService;
    private final ClientRepository clientRepository;
    private Client client;

    public TransfersController(TransferValidationService transferValidationService,
                               TransferService transferService,
                               ClientRepository clientRepository) {
        this.transferValidationService = transferValidationService;
        this.transferService = transferService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/do_transfer")
    public String transferToClient(@ModelAttribute("transfer") TransferEntity transferEntity, Principal principal, Model model) {
        client = clientRepository.findByUsername(principal.getName());
        List<Account> accounts = client.getAccounts().stream().filter(p -> !p.getType().equals(AccountType.SAVINGS_ACCOUNT)).toList();
        model.addAttribute("accounts", accounts);
        model.addAttribute("client", client);
        return "transfer_form";
    }

    @PostMapping("/do_transfer")
    public String doTransferToClient(@ModelAttribute("transfer") @Valid TransferEntity transferEntity, BindingResult bindingResult, Model model) {
        String errorSum = transferValidationService.validateTransferSum(transferEntity.getAccountSender(), transferEntity.getTransferSum());
        String errorRecipient = transferValidationService.validateRecipient(transferEntity.getAccountRecipient());
        if (!errorSum.isEmpty()) {
            ObjectError errSum = new ObjectError("globalError", errorSum);
            bindingResult.addError(errSum);
        }
        if (!errorRecipient.isEmpty()) {
            ObjectError errRecipient = new ObjectError("globalError", errorRecipient);
            bindingResult.addError(errRecipient);
        }
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
        List<Account> accounts = client.getAccounts().stream().filter(p -> !p.getType().equals(AccountType.SAVINGS_ACCOUNT)).toList();
        model.addAttribute("accounts", accounts);
        model.addAttribute("client", client);
        return "transfer_form";
    }

    @PostMapping("/between_their")
    public String doTransferBetweenTheir(@ModelAttribute("transfer") @Valid TransferEntity transferEntity, BindingResult bindingResult, Model model) {
        String err = transferValidationService.validateTransferSum(transferEntity.getAccountSender(), transferEntity.getTransferSum());
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", client);
            return "transfer_form";
        }
        transferService.transferBetweenTheir(transferEntity.getAccountSender(), transferEntity.getAccountRecipient(), transferEntity.getTransferSum());
        return "redirect:/profile";
    }
}
