package br.com.als.domain.exceptions;

public class DomainException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainException(String message) {
		super(message);
	}
}
