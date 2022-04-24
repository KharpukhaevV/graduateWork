package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Card;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findAllByClientId(long clientId);

}
