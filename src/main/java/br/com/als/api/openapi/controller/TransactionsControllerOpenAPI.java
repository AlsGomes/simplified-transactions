package br.com.als.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.als.api.dto.TransactionDTOInput;
import br.com.als.domain.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transações", description = "Efetuar novas transações e recuperar histórico de todas as que foram efetuadas")
public interface TransactionsControllerOpenAPI {

	@Operation(
			summary = "Efetuar transação", 
			description = "Para realizar a transação, o pagante (payer) deve conter ID 1 "
					+ "e o beneficiário (payee) deve conter o ID 2",
			responses = {
					@ApiResponse(responseCode = "201", description = "Recurso criado"),
					@ApiResponse(
							responseCode = "404", 
							description = "Pagante (payer) ou Beneficiário (payee) não encontrado",
							content = @Content(schema = @Schema)),
					@ApiResponse(
							responseCode = "400", 
							description = "Requisição errada",
							content = @Content(schema = @Schema)),
					@ApiResponse(
							responseCode = "422", 
							description = "Transação não autorizada",
							content = @Content(schema = @Schema)),
			})
	ResponseEntity<Transaction> transfer(
			@RequestBody(description = "Representação de uma transação", required = true) TransactionDTOInput input);

	@Operation(
			summary = "Recuperar transação por ID",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK"),
					@ApiResponse(
							responseCode = "404", 
							description = "Transação não encontrada",
							content = @Content(schema = @Schema)),
					@ApiResponse(
							responseCode = "400", 
							description = "Requisição errada",
							content = @Content(schema = @Schema)),
			})
	ResponseEntity<Transaction> getTransaction(
			@Parameter(description = "ID da transação", example = "1", required = true) Long id);

	@Operation(
			summary = "Recuperar todas as transações",
			responses = {
					@ApiResponse(responseCode = "200", description = "OK"),
					@ApiResponse(
							responseCode = "400", 
							description = "Requisição errada",
							content = @Content(schema = @Schema)),
			})
	ResponseEntity<List<Transaction>> getTransactions();
}
