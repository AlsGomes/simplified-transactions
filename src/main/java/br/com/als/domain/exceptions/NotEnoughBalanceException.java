package br.com.als.domain.exceptions;

public class NotEnoughBalanceException extends WalletException {
	private static final long serialVersionUID = 1L;

	public NotEnoughBalanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughBalanceException(String message) {
		super(message);
	}

}
