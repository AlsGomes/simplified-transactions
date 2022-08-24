package br.com.als.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ErrorType {

	OBJECT_NOT_FOUND("/object-not-found", "Object not found"),
	IDENTIFIER_ALREADY_EXISTS("/identifier-already-exists", "Identifier already exists"),
	UNATHORIZED_TRANSACTION("/unathorized-transaction", "Unathorized transaction"),
	MESSAGE_NOT_READABLE("/message-not-readable", "Message not readable"),
	INVALID_DATA("/invalid-data", "Invalid data"),
	WALLET("/wallet", "Wallet error");

	private String title;
	private String uri;

	private ErrorType(String path, String title) {
		this.title = title;
		this.uri = "https://transfer-app-heroku-app.com" + path;
	}

}
