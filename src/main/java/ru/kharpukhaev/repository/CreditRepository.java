package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.CreditEntity;
import ru.kharpukhaev.entity.enums.CreditStatus;

import java.util.List;

public interface CreditRepository extends CrudRepository<CreditEntity, Long> {
    List<CreditEntity> findAllByCreditStatus(CreditStatus creditStatus);

    List<CreditEntity> findAllByCardAndCreditStatus(Card card, CreditStatus creditStatus);
}
