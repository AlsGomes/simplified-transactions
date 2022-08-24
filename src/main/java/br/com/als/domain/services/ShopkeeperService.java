package br.com.als.domain.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.domain.exceptions.CnpjAlreadyExistsException;
import br.com.als.domain.exceptions.EmailAlreadyExistsException;
import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.model.Shopkeeper;
import br.com.als.domain.repositories.ShopkeeperRepository;

@Service
public class ShopkeeperService extends UserService {

	@Autowired
	private ShopkeeperRepository shopkeeperRepository;

	@Transactional
	public Shopkeeper addShopkeeper(@Valid Shopkeeper shopkeeper) {
		if (super.findByEmail(shopkeeper.getEmail()).isPresent())
			throw new EmailAlreadyExistsException(String.format(
					"Não foi possível adicionar o Lojista porque o e-mail %s já existe", shopkeeper.getEmail()));

		if (findByCnpj(shopkeeper.getCnpj()).isPresent())
			throw new CnpjAlreadyExistsException(String
					.format("Não foi possível adicionar o Lojista porque o CNPJ %s já existe", shopkeeper.getCnpj()));

		return shopkeeperRepository.save(shopkeeper);
	}

	public Optional<Shopkeeper> findByCnpj(String cnpj) {
		return shopkeeperRepository.findByCnpj(cnpj);
	}

	public Shopkeeper findByCnpjOrThrow(String cnpj) {
		return findByCnpj(cnpj).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Lojista com CNPJ %s não foi encontrado", cnpj)));
	}

	public Optional<Shopkeeper> findShopkeeperById(Long id) {
		var shopkeeper = super.findById(id);
		if (shopkeeper.isPresent())
			return Optional.of((Shopkeeper) shopkeeper.get());

		return Optional.of(null);
	}

	public Shopkeeper findShopkeeperByIdOrThrow(Long id) {
		return findShopkeeperById(id).orElseThrow(
				() -> new ObjectNotFoundException(String.format("Lojista com ID %s não foi encontrado", id)));
	}
}
