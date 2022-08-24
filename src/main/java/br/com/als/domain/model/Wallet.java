package br.com.als.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.als.domain.events.MoneyReceivedEvent;
import br.com.als.domain.exceptions.NegativeValueException;
import br.com.als.domain.exceptions.NotEnoughBalanceException;
import br.com.als.domain.exceptions.ShopkeeperTransferException;
import br.com.als.domain.exceptions.WalletException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(name = "wallet")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Wallet extends AbstractAggregateRoot<Wallet> {

	@Id
	@Column(name = "user_id")
	@EqualsAndHashCode.Include
	private Long id;

	@JsonIgnore
	@NotNull
	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	private User user;

	@NotNull
	private BigDecimal balance;

	public Wallet() {
		this.balance = new BigDecimal(0);
	}

	public Wallet(User user) {
		this();
		this.user = user;
	}

	public void addMoney(Double amount, User payer) {
		if (amount == null)
			throw new WalletException("Não foi possível adicionar dinheiro na carteira. Valor inexistente.");

		if (amount <= 0)
			throw new NegativeValueException(
					"Não foi possível adicionar dinheiro na carteira. Valor menor ou igual a zero");

		this.balance = this.balance.add(new BigDecimal(amount));

		registerEvent(new MoneyReceivedEvent(this.getUser(), payer, new BigDecimal(amount)));
	}

	public void withdrawMoney(Double amount) {
		if (isShopkeeperWallet())
			throw new ShopkeeperTransferException("Lojistas não podem transferir dinheiro, somente usuários comuns.");

		if (amount == null)
			throw new WalletException("Não foi possível retirar dinheiro da carteira. Valor inexistente.");

		if (amount <= 0)
			throw new NegativeValueException(
					"Não foi possível retirar dinheiro da carteira. Valor menor ou igual a zero.");

		if (!canWithdraw(amount))
			throw new NotEnoughBalanceException(
					"Não foi possível retirar dinheiro da carteira. Saldo insuficiente para completar a transação.");

		this.balance = this.balance.subtract(new BigDecimal(amount));
	}

	public boolean canWithdraw(Double amount) {
		if (isShopkeeperWallet())
			return false;

		if (amount == null)
			return false;

		if (amount <= 0)
			return false;

		return this.balance.subtract(new BigDecimal(amount)).doubleValue() >= 0;
	}

	@JsonIgnore
	public boolean isShopkeeperWallet() {
		return (this.getUser() instanceof Shopkeeper);
	}
}
