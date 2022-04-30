package ru.kharpukhaev.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kharpukhaev.entity.Contribution;

public interface ContributionsRepository extends CrudRepository<Contribution, Long> {
}
