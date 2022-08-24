package br.com.als.domain.services;

import br.com.als.domain.model.Transaction;

public interface TransactionsAuthorization {

	void authorize(Transaction transaction);
}
