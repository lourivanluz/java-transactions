package br.com.lourivanrluz.tutorial.transaction;

import org.springframework.data.repository.ListCrudRepository;

public interface TransactionsRepository extends ListCrudRepository<Transaction, Long> {
}
