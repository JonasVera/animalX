package com.br.animalx.animalX.dto;

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
public class ContatoDTO {
	
	private String id;
	
 	private String categoria; 
	
 	private String nome;
	
 	private String contato;
	
 	private String empresa;
	 
}
