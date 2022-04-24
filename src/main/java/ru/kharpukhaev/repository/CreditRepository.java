package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Credit;
import ru.kharpukhaev.entity.enums.CreditStatus;

import java.util.List;

public interface CreditRepository extends CrudRepository<Credit, Long> {
    List<Credit> findAllByStatus(CreditStatus status);
}
