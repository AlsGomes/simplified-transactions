package br.com.als.apitests;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import br.com.als.domain.services.TransactionsService;
import br.com.als.utils.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@TestPropertySource("/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsApiTests {

	private static final int TEST_TRANSACTION = 1;
	private static final int INEXISTENT_TRANSACTION_ID = 100;
	private static final String BASE_JSON_RESOURCE_PATH = "/json/";
	
	private int transactionsCount;
	
	private String jsonValidTransactionInput;
	private String jsonTransactionInputWithInexistentPayer;
	private String jsonTransactionInputWithInexistentPayee;
	private String jsonInvalidInputWithoutPayer;

	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway;
	
	@Autowired
	private TransactionsService transactionsService;
	
	@BeforeEach
	public void setup() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.basePath = "/transactions";
		RestAssured.port = port;
		
		jsonValidTransactionInput = ResourceUtils.getContentFromResource(BASE_JSON_RESOURCE_PATH + "valid-transaction-input.json");
		jsonTransactionInputWithInexistentPayer = ResourceUtils.getContentFromResource(BASE_JSON_RESOURCE_PATH + "valid-input-with-inexistent-payer.json");
		jsonTransactionInputWithInexistentPayee = ResourceUtils.getContentFromResource(BASE_JSON_RESOURCE_PATH + "valid-input-with-inexistent-payee.json");
		jsonInvalidInputWithoutPayer = ResourceUtils.getContentFromResource(BASE_JSON_RESOURCE_PATH + "invalid-input-without-payer.json");
		
		transactionsCount = (int) transactionsService.countTransactions();
		
		flyway.migrate();
	}

	@Test
	public void shouldReturnStatusOk_whenGetTransactions() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void shouldReturnCorrectStatusAndBody_whenGetOneTransaction() {
		given()
			.accept(ContentType.JSON)
			.pathParam("id", TEST_TRANSACTION)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("id", equalTo(TEST_TRANSACTION));
	}
	
	@Test
	public void shouldReturnStatusNotFound_whenTransactionNotExists() {
		given()
			.accept(ContentType.JSON)
			.pathParam("id", INEXISTENT_TRANSACTION_ID)
		.when()
			.get("/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void shouldReturnStatusCreated_whenMakeValidTransfer() {
		given()
			.body(jsonValidTransactionInput)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void shouldReturnStatusNotFound_whenMakeTransferWithInexistentPayer() {
		given()
			.body(jsonTransactionInputWithInexistentPayer)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void shouldReturnStatusNotFound_whenMakeTransferWithInexistentPayee() {
		given()
			.body(jsonTransactionInputWithInexistentPayee)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void shouldReturnStatusBadRequest_whenMakeTransferWithoutPayer() {
		given()
			.body(jsonInvalidInputWithoutPayer)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void shouldHaveCorrectCountOfTransactions_whenGetTransactions() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(transactionsCount));
	}
}
