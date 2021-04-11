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
public class ContatoDTO {

	private Long id;

	private Long animal;

	private String email;
	
	private String cidade;
	
	private String estado;

	private String tefone;

	private String hashAnimal;

}
