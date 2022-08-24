package br.com.als.domain.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.domain.exceptions.CpfAlreadyExistsException;
import br.com.als.domain.exceptions.EmailAlreadyExistsException;
import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.model.CommonUser;
import br.com.als.domain.repositories.CommonUserRepository;

@Service
public class CommonUserService extends UserService {

	@Autowired
	private CommonUserRepository commonUserRepository;

	@Transactional
	public CommonUser addCommonUser(@Valid CommonUser commonUser) {
		if (super.findByEmail(commonUser.getEmail()).isPresent())
			throw new EmailAlreadyExistsException(String.format(
					"Não foi possível adicionar o usuário porque o e-mail %s já existe", commonUser.getEmail()));

		if (findByCpf(commonUser.getCpf()).isPresent())
			throw new CpfAlreadyExistsException(String
					.format("Não foi possível adicionar o usuário porque o CPF %s já existe", commonUser.getCpf()));

		return commonUserRepository.save(commonUser);
	}

	public Optional<CommonUser> findByCpf(String cpf) {
		return commonUserRepository.findByCpf(cpf);
	}

	public CommonUser findByCpfOrThrow(String cpf) {
		return findByCpf(cpf).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Usuário com CPF %s não foi encontrado", cpf)));
	}

	public Optional<CommonUser> findCommonUserById(Long id) {
		var user = super.findById(id);
		if (user.isPresent())
			return Optional.of((CommonUser) super.findById(id).get());
	
		return Optional.of(null);
	}

	public CommonUser findCommonUserByIdOrThrow(Long id) {
		return findCommonUserById(id).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Usuário com ID %s não foi encontrado", id)));
	}
}
