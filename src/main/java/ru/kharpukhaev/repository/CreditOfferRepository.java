package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.CreditOfferEntity;
import ru.kharpukhaev.entity.enums.CreditBidStatus;

import java.util.List;

public interface CreditOfferRepository extends CrudRepository<CreditOfferEntity, Long> {
    List<CreditOfferEntity> findAllByStatus(CreditBidStatus status);
}
