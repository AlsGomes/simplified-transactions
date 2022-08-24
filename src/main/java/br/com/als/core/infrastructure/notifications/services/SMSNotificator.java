package br.com.als.core.infrastructure.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.als.core.infrastructure.notifications.model.SMSNotificatorModel;
import br.com.als.domain.model.User;
import br.com.als.domain.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SMSNotificator implements NotificationService {

	private static final String SMS_MESSAGE_FOR_SUCCESS = "Success";

	@Autowired
	private WebClient webClientSMSNotificator;

	@Override
	public void notify(User who, String msg) {
		Mono<SMSNotificatorModel> mono = webClientSMSNotificator
				.get()
				.uri(uriBuilder -> uriBuilder
						.path("/notify")
						.build())
				.retrieve()
				.bodyToMono(SMSNotificatorModel.class);

		mono.subscribe(smsModel -> {
			if (!smsModel.getMessage().equals(SMS_MESSAGE_FOR_SUCCESS)) {
				logError(who, msg);
				return;
			}

			log.info(String.format("O usuário %s foi notificado via SMS através do número %s com a mensagem: %s",
					who.getFullname(), who.getPhoneNumber(), msg));
		}, ex -> {
			logError(who, msg);
		});
	}

	private void logError(User who, String msg) {
		log.warn(String.format("Não foi possível notificar %s, com telefone %s, por SMS", who.getFullname(),
				who.getPhoneNumber()));
	}
}
