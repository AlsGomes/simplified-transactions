package br.com.als.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.als.api.dto.TransactionDTOInput;
import br.com.als.api.openapi.controller.TransactionsControllerOpenAPI;
import br.com.als.domain.model.Transaction;
import br.com.als.domain.services.TransactionsService;

@RestController
@RequestMapping("transactions")
public class TransactionsController implements TransactionsControllerOpenAPI {

	@Autowired
	private TransactionsService service;

	@PostMapping
	public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransactionDTOInput input) {
		var savedTransaction = service.transfer(input);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(savedTransaction.getId())
				.toUri();

		return ResponseEntity.created(uri).body(savedTransaction);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
		return ResponseEntity.ok(service.findByIdOrThrow(id));
	}
	
	@GetMapping
	public ResponseEntity<List<Transaction>> getTransactions() {
		return ResponseEntity.ok(service.findAll());
	}
}
