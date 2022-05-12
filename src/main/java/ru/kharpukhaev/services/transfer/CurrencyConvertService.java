package ru.kharpukhaev.services.transfer;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.enums.Currency;

@Service
public class CurrencyConvertService {

    public Long checkCurrencyAndConvert(Currency currencySender, Currency currencyRecipient, Long sum) {
        if (currencySender.equals(currencyRecipient)) {
            return sum;
        } else {
            if (currencySender.equals(Currency.RUB) && currencyRecipient.equals(Currency.USD)) {
               return rubToUsd(sum);
            }
            if (currencySender.equals(Currency.RUB) && currencyRecipient.equals(Currency.EUR)) {
                return rubToEur(sum);
            }
            if (currencySender.equals(Currency.USD) && currencyRecipient.equals(Currency.RUB)) {
                return usdToRub(sum);
            }
            if (currencySender.equals(Currency.USD) && currencyRecipient.equals(Currency.EUR)) {
                return usdToEur(sum);
            }
            if (currencySender.equals(Currency.EUR) && currencyRecipient.equals(Currency.USD)) {
                return eurToUsd(sum);
            }
            if (currencySender.equals(Currency.EUR) && currencyRecipient.equals(Currency.RUB)) {
                return eurToRub(sum);
            }
        }
        return sum;
    }

    private Long usdToRub(Long sum) {
        return sum * 70;
    }

    private Long usdToEur(Long sum) {
        return (long) (sum * 0.9);
    }

    private Long eurToRub(Long sum) {
        return sum * 80;
    }

    private Long eurToUsd(Long sum) {
        return (long) (sum * 1.1);
    }

    private Long rubToUsd(Long sum) {
        return (long) (sum * 0.015);
    }

    private Long rubToEur(Long sum) {
        return (long) (sum * 0.010);
    }
}
