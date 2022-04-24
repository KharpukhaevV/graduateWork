package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.enums.CreditStatus;

import java.util.List;

public interface CreditBidRepository extends CrudRepository<CreditBid, Long> {
    List<CreditBid> findAllByStatus(CreditStatus status);
}
