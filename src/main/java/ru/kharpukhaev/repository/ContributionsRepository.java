package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.Contribution;

import java.util.List;

public interface ContributionsRepository extends CrudRepository<Contribution, Long> {
    List<Contribution> findAllByClient(Client client);

    List<Contribution> findAllByExpirationDate(String expirationDate);
}
