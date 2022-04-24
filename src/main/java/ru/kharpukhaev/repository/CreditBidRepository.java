package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.CreditBid;
import ru.kharpukhaev.entity.enums.CreditBidStatus;

import java.util.List;

public interface CreditBidRepository extends CrudRepository<CreditBid, Long> {
    List<CreditBid> findAllByStatus(CreditBidStatus status);
}
