package br.com.als.domain.exceptions;

public class CnpjAlreadyExistsException extends IdentifierAlreadyExistsException {
	private static final long serialVersionUID = 1L;

	public CnpjAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CnpjAlreadyExistsException(String message) {
		super(message);
	}
}
