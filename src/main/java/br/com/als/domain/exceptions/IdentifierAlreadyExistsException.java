package br.com.als.domain.exceptions;

public class IdentifierAlreadyExistsException extends DomainException {
	private static final long serialVersionUID = 1L;

	public IdentifierAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdentifierAlreadyExistsException(String message) {
		super(message);
	}
}
