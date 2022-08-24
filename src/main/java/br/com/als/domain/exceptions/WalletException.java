package br.com.als.domain.exceptions;

public class WalletException extends DomainException {
	private static final long serialVersionUID = 1L;

	public WalletException(String message, Throwable cause) {
		super(message, cause);
	}

	public WalletException(String message) {
		super(message);
	}
}
