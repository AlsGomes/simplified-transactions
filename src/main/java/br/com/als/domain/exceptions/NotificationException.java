package br.com.als.domain.exceptions;

public class NotificationException extends DomainException {
	private static final long serialVersionUID = 1L;

	public NotificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotificationException(String message) {
		super(message);
	}
}
