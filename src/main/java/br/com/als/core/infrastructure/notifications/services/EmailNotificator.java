package br.com.als.core.infrastructure.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.als.core.infrastructure.notifications.model.EmailNotificatorModel;
import br.com.als.domain.model.User;
import br.com.als.domain.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class EmailNotificator implements NotificationService {

	private static final String EMAIL_MESSAGE_FOR_SUCCESS = "Success";
	
	@Autowired
	private WebClient webClientEmailNotificator;	

	@Override
	public void notify(User who, String msg) {
		Mono<EmailNotificatorModel> mono = webClientEmailNotificator
				.get()
				.uri(uriBuilder -> uriBuilder
						.path("/notify")
						.build())
				.retrieve()
				.bodyToMono(EmailNotificatorModel.class);

		mono.subscribe(emailModel -> {
			if (!emailModel.getMessage().equals(EMAIL_MESSAGE_FOR_SUCCESS)) {
				logError(who, msg);
				return;
			}

			log.info(String.format("O usuário %s foi notificado via e-mail %s com a mensagem: %s", 
					who.getFullname(),
					who.getEmail(), 
					msg));
		}, ex -> {
			logError(who, msg);
		});		
	}
	
	private void logError(User who, String msg) {
		log.warn(String.format("Não foi possível notificar %s, com e-mail %s", 
				who.getFullname(), 
				who.getEmail()));
	}
}
