package br.com.als.core.infrastructure.transactionauthorization.services;

import java.net.URI;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.als.core.infrastructure.transactionauthorization.model.VISAAuthorizationModel;
import br.com.als.domain.exceptions.UnathorizedTransactionException;
import br.com.als.domain.model.Transaction;
import br.com.als.domain.services.TransactionsAuthorization;

@Primary
@Component
public class VISATransactionsAuthorization implements TransactionsAuthorization {

	private static final URI VISA_URL = URI.create("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6");
	private static final String IS_AUTHORIZED_MESSAGE = "Autorizado";

	@Override
	public void authorize(Transaction transaction) {
		RestTemplate restTemplate = new RestTemplate();

		VISAAuthorizationModel authModel = restTemplate.getForObject(VISA_URL, VISAAuthorizationModel.class);

		if (!authModel.getMessage().equals(IS_AUTHORIZED_MESSAGE))
			throw new UnathorizedTransactionException("Não foi possível autorizar a transferência VISA");
	}
}
