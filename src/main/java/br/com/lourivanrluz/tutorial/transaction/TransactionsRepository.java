package br.com.lourivanrluz.tutorial.transaction;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

public interface TransactionsRepository extends ListCrudRepository<Transaction, UUID> {
}
