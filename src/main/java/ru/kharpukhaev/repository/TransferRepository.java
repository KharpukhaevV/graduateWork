package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.TransferEntity;
import ru.kharpukhaev.entity.enums.TransferStatus;

import java.util.List;

public interface TransferRepository extends CrudRepository<TransferEntity, Long> {
    List<TransferEntity> findAllBySenderId(long id);

    List<TransferEntity> findAllByStatus(TransferStatus status);
}
