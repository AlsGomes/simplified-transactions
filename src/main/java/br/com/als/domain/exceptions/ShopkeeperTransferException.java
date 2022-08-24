package br.com.als.domain.exceptions;

public class ShopkeeperTransferException extends WalletException {
	private static final long serialVersionUID = 1L;

	public ShopkeeperTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShopkeeperTransferException(String message) {
		super(message);
	}

}
