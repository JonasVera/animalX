package com.animalx.AnimalX.dto;
 
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
  
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEditDTO {
	 
	private String id;
	@NotNull
	private String nome;
	@NotNull
	private String email;
	@NotNull
	private String senha;
	  
}
