package ru.kharpukhaev.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;
import ru.kharpukhaev.entity.enums.TransferStatus;
import ru.kharpukhaev.repository.ClientRepository;
import ru.kharpukhaev.repository.CreditBidRepository;
import ru.kharpukhaev.repository.TransferRepository;
import ru.kharpukhaev.services.OperatorCheck;

@Controller
@RequestMapping("/operator")
@PreAuthorize("hasAuthority('MODERATOR')")
public class OperatorController {

    private final ClientRepository clientRepository;

    private final TransferRepository transferRepository;

    private final CreditBidRepository creditBidRepository;

    private final OperatorCheck operatorCheck;

    public OperatorController(ClientRepository clientRepository, TransferRepository transferRepository, CreditBidRepository creditBidRepository, OperatorCheck operatorCheck) {
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.creditBidRepository = creditBidRepository;
        this.operatorCheck = operatorCheck;
    }


    @GetMapping("/payments")
    public String payments(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("transfers", transferRepository.findAllByStatus(TransferStatus.PROCESSED));
        return "operator_payments";
    }

    @PostMapping("/payments/accept")
    public String accept(@RequestParam TransferEntity transfer) {
        operatorCheck.paymentAccept(transfer);
        return "redirect:/operator/payments";
    }

    @PostMapping("/payments/decline")
    public String decline(@RequestParam TransferEntity transfer) {
        operatorCheck.paymentDecline(transfer);
        return "redirect:/operator/payments";
    }

    @GetMapping("/credits")
    public String credits(Model model) {
        model.addAttribute("credits", creditBidRepository.findAllByStatus(CreditStatus.PROCESSED));
        return "operator_credits";
    }

    @PostMapping("/offer")
    public String creditOffer(@RequestParam CreditBid creditBid, @RequestParam long limit, @RequestParam double percent, @RequestParam String date) {
        operatorCheck.makeCreditOffer(creditBid, limit, percent, date);
        return "redirect:/operator/credits";
    }
}
