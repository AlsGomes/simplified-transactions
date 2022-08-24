package br.com.als.domain.exceptions;

public class UnathorizedTransactionException extends DomainException {
	private static final long serialVersionUID = 1L;

	public UnathorizedTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnathorizedTransactionException(String message) {
		super(message);
	}
}
