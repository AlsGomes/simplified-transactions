package br.com.als.domain.exceptions;

public class NegativeValueException extends WalletException {
	private static final long serialVersionUID = 1L;

	public NegativeValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public NegativeValueException(String message) {
		super(message);
	}

}
