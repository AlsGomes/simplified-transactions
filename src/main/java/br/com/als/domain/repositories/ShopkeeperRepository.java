package br.com.als.domain.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.als.domain.model.Shopkeeper;

@Repository
public interface ShopkeeperRepository extends UserRepository {

	Optional<Shopkeeper> findByCnpj(String cnpj);
}
