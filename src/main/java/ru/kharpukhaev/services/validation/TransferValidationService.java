package ru.kharpukhaev.services.validation;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.repository.AccountRepository;

@Service
public class TransferValidationService {

    private final AccountRepository accountRepository;

    public TransferValidationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String validateTransferSum(String sender, long sum) {
        Account accountSender = accountRepository.findAccountByNumber(sender);
        if (accountSender.getBalance() < sum) {
            return "Недостаточно средств";
        }
        return "";
    }

    public String validateRecipient(String recipient) {
        Account accountRecipient = accountRepository.findAccountByNumber(recipient);
        if (accountRecipient == null) {
            return "Получатель не найден";
        }
        return "";
    }
}
