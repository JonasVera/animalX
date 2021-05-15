package com.animalx.AnimalX.entity;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDados {

	private Long qtdUsuariosCadastrados;
	private Long qtdAnimaisCadastrados;
	private Long qtdAnimaisAdotados;
	
}
