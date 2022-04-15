package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.TransferEntity;

import java.util.List;

public interface TransferRepository extends CrudRepository<TransferEntity, Long> {
    List<TransferEntity> findAllBySenderId(long id);
}
