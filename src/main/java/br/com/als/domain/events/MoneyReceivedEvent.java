package br.com.als.domain.events;

import java.math.BigDecimal;

import br.com.als.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoneyReceivedEvent {

	private User payee;
	private User payer;
	private BigDecimal amount;
}
