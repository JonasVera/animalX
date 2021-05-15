package com.animalx.AnimalX.service;
 
import java.util.List;
import java.util.Objects;
import java.util.Optional; 
import javax.transaction.Transactional;

import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Foto; 
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FotoService {

	@Autowired	
	private FotoRepository repository;
 
	 
	@Transactional 
	public  Foto adicioanarFoto(Foto foto) { 
		return repository.save(foto); 
	}
	
	@Transactional 
	public void deletarFoto(Foto foto) {
		Objects.requireNonNull(foto.getId());
		if (foto.getId() == null)
	 		throw new RegraNegocioException("Identificador inválido !");
		 
		if (obterPorId(foto.getId()).isPresent())
			repository.delete(foto); 
		else
			throw new RegraNegocioException("Esta foto não existe na base de dados.");
	}
	 
	public Optional<Foto> obterPorId(Long id) {
		Objects.requireNonNull(id);
		 
		return repository.findById(id);
	}
	
	public List<Foto> obterPorAnimal(Animal animal) {
		Objects.requireNonNull(animal.getId());
		 
		return repository.findByAnimal(animal);
	}
	
}
