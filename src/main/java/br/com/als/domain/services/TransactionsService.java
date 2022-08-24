package br.com.als.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.als.api.dto.TransactionDTOInput;
import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.exceptions.UnathorizedTransactionException;
import br.com.als.domain.model.Transaction;
import br.com.als.domain.repositories.TransactionsRepository;
import br.com.als.domain.repositories.WalletRepository;

@Service
public class TransactionsService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionsRepository transactionsRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionsAuthorization transactionAuthorization;

	@Transactional
	public Transaction transfer(TransactionDTOInput input) {
		if (input.isSameUser())
			throw new UnathorizedTransactionException("Transferências não podem ser realizadas para si próprio.");

		var payee = userService.findByIdOrThrow(input.getPayee());
		var payer = userService.findByIdOrThrow(input.getPayer());

		payer.getWallet().withdrawMoney(input.getAmount().doubleValue());
		payee.getWallet().addMoney(input.getAmount().doubleValue(), payer);

		var transaction = new Transaction();
		transaction.setAmount(input.getAmount());
		transaction.setPayee(payee);
		transaction.setPayer(payer);

		transactionAuthorization.authorize(transaction);

		var savedTransaction = transactionsRepository.save(transaction);
		walletRepository.save(payer.getWallet());
		walletRepository.save(payee.getWallet());

		return savedTransaction;
	}

	public Transaction findByIdOrThrow(Long id) {
		return transactionsRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException(String.format("A transação com ID %s não existe.", id)));
	}

	public List<Transaction> findAll() {
		return transactionsRepository.findAll();
	}

	public long countTransactions() {
		return transactionsRepository.count();
	}
}
