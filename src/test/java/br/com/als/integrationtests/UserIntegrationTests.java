package br.com.als.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import br.com.als.domain.exceptions.CnpjAlreadyExistsException;
import br.com.als.domain.exceptions.CpfAlreadyExistsException;
import br.com.als.domain.exceptions.EmailAlreadyExistsException;
import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.model.CommonUser;
import br.com.als.domain.model.Shopkeeper;
import br.com.als.domain.services.CommonUserService;
import br.com.als.domain.services.ShopkeeperService;
import br.com.als.utils.DatabaseCleaner;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserIntegrationTests {

	@Autowired
	private CommonUserService commonUserService;

	@Autowired
	private ShopkeeperService shopkeeperService;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	private static final Long NOT_EXISTENT_USER_ID = 999L;

	private Shopkeeper shopkeeper1;
	private Shopkeeper shopkeeper2;
	private CommonUser commonUser1;

	@BeforeEach
	private void setup() {
		databaseCleaner.clearTables();
		prepareData();
	}

	@Test
	public void shouldAddShopkeeper_WhenItsCorrect() {
		Shopkeeper shopkeeper = new Shopkeeper();
		shopkeeper.setFullname("Lojista X");
		shopkeeper.setEmail("lojista@gmail.com");
		shopkeeper.setPhoneNumber("+5511911449071");
		shopkeeper.setCnpj("52708112000103");
		shopkeeper.setPwd("senha123");

		var savedUser = shopkeeperService.addShopkeeper(shopkeeper);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isNotNull();
	}

	@Test
	public void shouldAddCommonUser_WhenItsCorrect() {
		CommonUser userY = new CommonUser();
		userY.setFullname("Usuário Y");
		userY.setEmail("usuarioY@gmail.com");
		userY.setPhoneNumber("+5511911449071");
		userY.setCpf("43512398745");
		userY.setPwd("senha123");

		var savedUser = commonUserService.addCommonUser(userY);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isNotNull();
	}

	@Test
	public void shouldRemoveCommonUser_WhenExists() {
		commonUserService.remove(commonUser1.getId());
	}

	@Test
	public void shouldRemoveShopkeeper_WhenExists() {
		shopkeeperService.remove(shopkeeper1.getId());
	}

	@Test
	public void shouldFail_WhenRemovingCommonUserThatDoesntExist() {
		ObjectNotFoundException ex = Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			commonUserService.remove(NOT_EXISTENT_USER_ID);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddShopkeeperWithExistentEmail() {
		Shopkeeper newShopkeeper = new Shopkeeper();
		BeanUtils.copyProperties(shopkeeper1, newShopkeeper, "id", "wallet");

		EmailAlreadyExistsException ex = Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
			shopkeeperService.addShopkeeper(newShopkeeper);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddShopkeeperWithExistentCnpj() {
		Shopkeeper newShopkeeper = new Shopkeeper();
		BeanUtils.copyProperties(shopkeeper1, newShopkeeper, "id", "wallet");
		newShopkeeper.setEmail("newemail@gmail.com");

		CnpjAlreadyExistsException ex = Assertions.assertThrows(CnpjAlreadyExistsException.class, () -> {
			shopkeeperService.addShopkeeper(newShopkeeper);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddCommonUserWithExistentEmail() {
		CommonUser newCommonUser = new CommonUser();
		BeanUtils.copyProperties(commonUser1, newCommonUser, "id", "wallet");

		EmailAlreadyExistsException ex = Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
			commonUserService.addCommonUser(newCommonUser);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddCommonUserWithExistentCpf() {
		CommonUser newCommonUser = new CommonUser();
		BeanUtils.copyProperties(commonUser1, newCommonUser, "id", "wallet");
		newCommonUser.setEmail("newemail@gmail.com");

		CpfAlreadyExistsException ex = Assertions.assertThrows(CpfAlreadyExistsException.class, () -> {
			commonUserService.addCommonUser(newCommonUser);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddInvalidCommonUser() {
		CommonUser userY = new CommonUser();
		userY.setEmail("usuarioY@gmail.com");
		userY.setPhoneNumber("+5511911449071");
		userY.setCpf("43512398745");
		userY.setPwd("senha123");

		ConstraintViolationException ex = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			commonUserService.addCommonUser(userY);
		});

		assertThat(ex).isNotNull();
	}

	@Test
	public void shouldFail_WhenAddInvalidShopkeeper() {
		Shopkeeper shopkeeperY = new Shopkeeper();
		shopkeeperY.setEmail("usuarioY@gmail.com");
		shopkeeperY.setFullname("Lojista Y");
		shopkeeperY.setCnpj("43512398745");
		shopkeeperY.setPwd("senha123");

		ConstraintViolationException ex = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			shopkeeperService.addShopkeeper(shopkeeperY);
		});

		assertThat(ex).isNotNull();
	}

	private void prepareData() {
		shopkeeper1 = new Shopkeeper();
		shopkeeper1.setFullname("Lojista Y");
		shopkeeper1.setEmail("lojistay@gmail.com");
		shopkeeper1.setPhoneNumber("+5511911449071");
		shopkeeper1.setCnpj("09541374000199");
		shopkeeper1.setPwd("senha123");

		shopkeeper2 = new Shopkeeper();
		shopkeeper2.setFullname("Lojista Z");
		shopkeeper2.setEmail("lojistaz@gmail.com");
		shopkeeper2.setPhoneNumber("+5511911449071");
		shopkeeper2.setCnpj("54876321000177");
		shopkeeper2.setPwd("senha123");

		commonUser1 = new CommonUser();
		commonUser1.setFullname("Usuário X");
		commonUser1.setEmail("usuarioX@gmail.com");
		commonUser1.setPhoneNumber("+5511911449071");
		commonUser1.setCpf("43587498812");
		commonUser1.setPwd("senha123");

		shopkeeperService.addShopkeeper(shopkeeper1);
		shopkeeperService.addShopkeeper(shopkeeper2);
		commonUserService.addCommonUser(commonUser1);
	}
}
