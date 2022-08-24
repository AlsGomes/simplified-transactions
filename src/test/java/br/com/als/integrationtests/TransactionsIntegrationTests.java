package br.com.als.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import br.com.als.api.dto.TransactionDTOInput;
import br.com.als.domain.exceptions.NegativeValueException;
import br.com.als.domain.exceptions.NotEnoughBalanceException;
import br.com.als.domain.exceptions.ShopkeeperTransferException;
import br.com.als.domain.exceptions.UnathorizedTransactionException;
import br.com.als.domain.model.CommonUser;
import br.com.als.domain.model.Shopkeeper;
import br.com.als.domain.services.CommonUserService;
import br.com.als.domain.services.ShopkeeperService;
import br.com.als.domain.services.TransactionsService;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class TransactionsIntegrationTests {

	private static final Long MASTER_SHOPKEEPER_ID = 2L;
	private static final Long MASTER_COMMON_USER_ID = 1L;

	@Autowired
	private TransactionsService transactionsService;

	@Autowired
	private Flyway flyway;

	@Autowired
	private CommonUserService commonUserService;

	@Autowired
	private ShopkeeperService shopkeeperService;

	private CommonUser masterCommonUser;
	private Shopkeeper masterShopkeeper;

	@BeforeEach
	private void setup() {
		flyway.migrate();
		prepareData();
	}

	@Test
	public void shouldTransferMoney_WhenHasValidData() {
		TransactionDTOInput transaction = TransactionDTOInput.builder()
				.amount(new BigDecimal(25))
				.payer(masterCommonUser.getId())
				.payee(masterShopkeeper.getId())
				.build();

		var savedTransaction = transactionsService.transfer(transaction);

		System.out.println("Waiting to see notifications");
		sleep(11 * 1000l);

		assertThat(savedTransaction).isNotNull();
		assertThat(savedTransaction.getId()).isNotNull();
	}

	@Test
	public void shouldFail_WhenPayerHasNotEnoughBalance() {
		TransactionDTOInput transaction = TransactionDTOInput.builder()
				.amount(new BigDecimal(1500))
				.payer(masterCommonUser.getId())
				.payee(masterShopkeeper.getId())
				.build();

		NotEnoughBalanceException ex = Assertions.assertThrows(NotEnoughBalanceException.class, () -> {
			transactionsService.transfer(transaction);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenTransactionWithNegativeValue() {
		TransactionDTOInput transaction = TransactionDTOInput.builder()
				.amount(new BigDecimal(-10))
				.payer(masterCommonUser.getId())
				.payee(masterShopkeeper.getId())
				.build();

		NegativeValueException ex = Assertions.assertThrows(NegativeValueException.class, () -> {
			transactionsService.transfer(transaction);
		});

		assertThat(ex).isNotNull();
	}
	
	@Test
	public void shouldFail_WhenTransactionHasPayeeEqualsToPayer() {
		TransactionDTOInput transaction = TransactionDTOInput.builder()
				.amount(new BigDecimal(100))
				.payer(masterCommonUser.getId())
				.payee(masterCommonUser.getId())
				.build();
		
		UnathorizedTransactionException ex = Assertions.assertThrows(UnathorizedTransactionException.class, () -> {
			transactionsService.transfer(transaction);
		});
		
		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenShopkeeperTransferMoney() {
		TransactionDTOInput transaction = TransactionDTOInput.builder()
				.amount(new BigDecimal(100))
				.payee(masterCommonUser.getId())
				.payer(masterShopkeeper.getId())
				.build();

		ShopkeeperTransferException ex = Assertions.assertThrows(ShopkeeperTransferException.class, () -> {
			transactionsService.transfer(transaction);
		});

		assertThat(ex).isNotNull();
	}

	private void prepareData() {
		masterShopkeeper = shopkeeperService.findShopkeeperByIdOrThrow(MASTER_SHOPKEEPER_ID);
		masterCommonUser = commonUserService.findCommonUserByIdOrThrow(MASTER_COMMON_USER_ID);
	}
	
	private void sleep(long milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
