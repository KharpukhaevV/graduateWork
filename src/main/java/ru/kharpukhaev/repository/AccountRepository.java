package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findAccountByNumber(String number);
}
