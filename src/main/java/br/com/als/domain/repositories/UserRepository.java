package br.com.als.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.als.domain.model.User;

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
