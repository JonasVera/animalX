package com.animalx.AnimalX.service;

import java.util.List; 
import javax.transaction.Transactional;

import com.animalx.AnimalX.entity.Foto;
import com.animalx.AnimalX.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FotoService {

	@Autowired	
	private FotoRepository repository;

	private String raiz = "C:\\testeupload";
	 
	private String diretorioFotos = "\\fotos";
	
	@Transactional 
	public  Foto adicioanarFoto(Foto foto) {
		 
		return repository.save(foto);
			 
	}
	
	@Transactional 
	public void deletarFoto( Foto foto) {
		  repository.delete(foto);
			 
	}
	@Transactional 
	public List<Foto> buscarContatoEmpresa() {
		return repository.findAll();  
	}
	
}
