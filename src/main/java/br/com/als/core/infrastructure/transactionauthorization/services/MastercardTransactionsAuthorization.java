package br.com.als.core.infrastructure.transactionauthorization.services;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.als.core.infrastructure.transactionauthorization.model.MastercardAuthorizationModel;
import br.com.als.domain.exceptions.UnathorizedTransactionException;
import br.com.als.domain.model.Transaction;
import br.com.als.domain.services.TransactionsAuthorization;

@Component
public class MastercardTransactionsAuthorization implements TransactionsAuthorization {

	private static final URI MASTERCARD_URL = URI
			.create("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6");
	private static final String IS_AUTHORIZED_MESSAGE = "Autorizado";

	@Override
	public void authorize(Transaction transaction) {
		RestTemplate restTemplate = new RestTemplate();

		MastercardAuthorizationModel authModel = restTemplate.getForObject(MASTERCARD_URL,
				MastercardAuthorizationModel.class);

		if (!authModel.getMessage().equals(IS_AUTHORIZED_MESSAGE))
			throw new UnathorizedTransactionException("Não foi possível autorizar a transferência Mastercard");
	}
}
