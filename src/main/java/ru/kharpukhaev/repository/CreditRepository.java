package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Credit;

public interface CreditRepository extends CrudRepository<Credit, Long> {

}
