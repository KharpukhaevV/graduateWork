package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Card;
import ru.kharpukhaev.entity.enums.CardType;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findAllByClientId(long clientId);

    List<Card> findAllByType(CardType type);

}
