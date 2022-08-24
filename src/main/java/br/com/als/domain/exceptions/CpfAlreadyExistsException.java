package br.com.als.domain.exceptions;

public class CpfAlreadyExistsException extends IdentifierAlreadyExistsException {
	private static final long serialVersionUID = 1L;

	public CpfAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CpfAlreadyExistsException(String message) {
		super(message);
	}
}
