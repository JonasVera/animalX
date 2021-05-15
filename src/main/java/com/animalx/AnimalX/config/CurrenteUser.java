package com.animalx.AnimalX.config;

import com.animalx.AnimalX.entity.Usuario; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenteUser {

	private String token;
	private Usuario usuario;
	
	 
}
