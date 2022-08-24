package br.com.als.domain.exceptions;

public class EmailAlreadyExistsException extends IdentifierAlreadyExistsException {
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
