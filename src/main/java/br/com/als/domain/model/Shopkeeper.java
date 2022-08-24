package br.com.als.domain.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shopkeeper")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Shopkeeper extends User {

	@NotBlank
	private String cnpj;

	public Shopkeeper() {
		super();
	}

	public Shopkeeper(String fullname, String email, String phoneNumber, String password, String cnpj) {
		super(fullname, email, phoneNumber, password);
		this.cnpj = cnpj;
	}
}
