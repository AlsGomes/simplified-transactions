package br.com.als.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TransactionDTOInput {
	
	@Schema(example = "1")
	@NotNull
	private Long payer;

	@Schema(example = "2")
	@NotNull
	private Long payee;

	@NotNull
	@DecimalMin("0.01")
	private BigDecimal amount;

	@JsonIgnore
	public boolean isSameUser() {
		return payer.equals(payee);
	}
}
