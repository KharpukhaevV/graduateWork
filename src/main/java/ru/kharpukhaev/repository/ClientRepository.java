package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findByLogin(String login);
    List<Client> findByLastnameAndFirstname(String lastname, String firstname);
    Client findFirstById(long id);
    Client findByCardNumber(String cardNumber);
}