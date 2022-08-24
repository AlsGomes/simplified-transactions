package br.com.als.domain.services;

import br.com.als.domain.model.User;

public interface NotificationService {

	void notify(User who, String msg);
}
