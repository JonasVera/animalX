package com.animalx.AnimalX.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mensagem {
private String remetente;
	
	private List<String> destinatarios;
	
	private String assunto;
	
	private String corpo;
	
	
}
