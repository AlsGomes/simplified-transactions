package br.com.als.domain.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "common_user")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class CommonUser extends User {

	@NotBlank
	private String cpf;

	public CommonUser() {
		super();
	}

	public CommonUser(String fullname, String email, String phoneNumber, String password, String cpf) {
		super(fullname, email, phoneNumber, password);
		this.cpf = cpf;
	}
}
