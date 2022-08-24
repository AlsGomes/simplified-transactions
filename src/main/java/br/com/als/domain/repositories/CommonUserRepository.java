package br.com.als.domain.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.als.domain.model.CommonUser;

@Repository
public interface CommonUserRepository extends UserRepository {

	Optional<CommonUser> findByCpf(String cpf);
}
