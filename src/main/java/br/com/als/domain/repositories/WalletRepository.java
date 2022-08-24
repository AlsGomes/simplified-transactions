package br.com.als.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.als.domain.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
