package ru.kharpukhaev.services.validation;


import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.kharpukhaev.entity.Account;
import ru.kharpukhaev.repository.AccountRepository;


@Service

public class ValidationService {


    private final AccountRepository accountRepository;


    public ValidationService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;

    }


    public void validateTransferSum(String sender, long sum, BindingResult bindingResult) {

        Account accountSender = accountRepository.findAccountByNumber(sender);

        if (accountSender != null) {

            if (accountSender.getBalance() < sum) {

                ObjectError errSum = new ObjectError("globalError", "Недостаточно средств");

                bindingResult.addError(errSum);

            }

        }

    }


    public void validateAccount(String accountNum, BindingResult bindingResult) {

        Account accountRecipient = accountRepository.findAccountByNumber(accountNum);

        if (accountRecipient == null) {

            ObjectError errSum = new ObjectError("globalError", "Некоректный номер");

            bindingResult.addError(errSum);

        }

    }


}