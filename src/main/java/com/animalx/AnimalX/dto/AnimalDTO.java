package com.animalx.AnimalX.dto;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

	private Long id;
	private String apelido;
	private String especie;
	private String raca;
	private String descricao;
	private String peso;
	private String altura;
	private String idade;
	private String categoria;
	private String situacao;
	private String cidade;
	private String estado;
	private Long usuario;
	private String sexo;
 

}
