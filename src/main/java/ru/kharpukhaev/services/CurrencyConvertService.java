package ru.kharpukhaev.services;

import org.springframework.stereotype.Service;
import ru.kharpukhaev.entity.enums.Currency;

@Service
public class CurrencyConvertService {

    public Long checkCurrencyAndConvert(Currency currency1, Currency currency2, Long sum) {
        if (currency1.equals(currency2)) {
            return sum;
        } else {
            if (currency1.equals(Currency.RUB) && currency2.equals(Currency.USD)) {
               return rubToUsd(sum);
            }
            if (currency1.equals(Currency.RUB) && currency2.equals(Currency.EUR)) {
                return rubToEur(sum);
            }
            if (currency1.equals(Currency.USD) && currency2.equals(Currency.RUB)) {
                return usdToRub(sum);
            }
            if (currency1.equals(Currency.USD) && currency2.equals(Currency.EUR)) {
                return usdToEur(sum);
            }
            if (currency1.equals(Currency.EUR) && currency2.equals(Currency.USD)) {
                return eurToUsd(sum);
            }
            if (currency1.equals(Currency.EUR) && currency2.equals(Currency.RUB)) {
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
