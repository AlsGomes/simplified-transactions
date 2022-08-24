package br.com.als.core.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	private static final String BASE_SMS_URL = "https://o4d9z.mocklab.io";
	private static final String BASE_EMAIL_URL = "https://o4d9z.mocklab.io";

	@Bean
	public WebClient webClientSMSNotificator(WebClient.Builder builder) {
		return builder
				.baseUrl(BASE_SMS_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	@Bean
	public WebClient webClientEmailNotificator(WebClient.Builder builder) {
		return builder
				.baseUrl(BASE_EMAIL_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
