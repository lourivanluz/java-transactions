package br.com.lourivanrluz.tutorial.wallet;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, UUID> {

}
