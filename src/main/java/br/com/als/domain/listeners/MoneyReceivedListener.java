package br.com.als.domain.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.als.domain.events.MoneyReceivedEvent;
import br.com.als.domain.services.NotificationService;

@Component
public class MoneyReceivedListener {

	@Autowired
	List<NotificationService> notificators;

	@EventListener
	public void onMoneyReceived(MoneyReceivedEvent event) {
		String msg = String.format("\nOlá, %s\n\nVocê acabou de receber %.2f do pagante %s.",
				event.getPayee().getFullname(), 
				event.getAmount(), 
				event.getPayer().getFullname());

		notificators.forEach(notificator -> notificator.notify(event.getPayee(), msg));
	}
}
