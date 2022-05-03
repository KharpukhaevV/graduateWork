package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.ClientCreditRequest;
import ru.kharpukhaev.entity.enums.CreditBidStatus;

import java.util.List;

public interface ClientCreditRequestRepository extends CrudRepository<ClientCreditRequest, Long> {
    List<ClientCreditRequest> findAllByStatus(CreditBidStatus status);
}
