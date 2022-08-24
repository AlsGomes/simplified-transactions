package br.com.als.domain.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.model.User;
import br.com.als.domain.repositories.CommonUserRepository;

@Service
public class UserService {

	/**
	 * Using CommonUserRepository as the main Service to return User Type.
	 * UserRepository is a @NoRepositoryBean
	 */
	@Autowired
	private CommonUserRepository userRepository;

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public User findByIdOrThrow(Long id) {
		return findById(id).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Usuário com ID %s não foi encontrado", id)));
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findByEmailOrThrow(String email) {
		return findByEmail(email).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Usuário com E-mail %s não foi encontrado", email)));
	}

	@Transactional
	public void remove(Long id) {
		var user = findByIdOrThrow(id);
		userRepository.delete(user);
	}
}
