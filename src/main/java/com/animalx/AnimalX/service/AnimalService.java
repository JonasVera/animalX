package com.animalx.AnimalX.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AnimalService {

	@Autowired
	private AnimalRepository repository;
	
	@Autowired 
	private UsuarioService userService;
	
	@Transactional 
	public Animal cadastrarAnimalComUsuario(Animal animal) {
		
		Optional<Usuario> user = userService.obterPorId(animal.getUsuario().getId());

		if (user.isPresent()) {
			 return repository.save(animal);
		} else {
			throw new RegraNegocioException("Não foi possivel cadastrar o animal, usuario não encontrado.");
		}

	}
	 
	@Transactional 
	public List<Animal> buscar(Animal animalFiltro) {
		Example<Animal> example = Example.of(animalFiltro);
		 if (example == null)
			 return repository.findAll();
		return repository.findAll(example);
	}
	
	@Transactional 
	public Optional<Animal> buscarPorId(Animal animal) {
	 
		 if	(animal.getUsuario().getId() != null) {
			 return repository.findById(animal.getId());
		 }else {
			 throw new RegraNegocioException("Animal não encontrado para este usúario.");
		 } 
	}
 
	public void alterarAnimal(Animal animal) {
		repository.save(animal);
	}
	
	
	public void adotar(Long id) {
		Animal animal = repository.findById(id).get();
		if (animal.getId() != null) {
			animal.setSituacao("ADOTADO");
			repository.save(animal);
		} 	
	} 
	@Transactional 
	public Optional<Animal> obterPorId(Long id) {
		return repository.findById(id);
	}
	@Transactional 
	public Page<Animal> listAnimais (Pageable paginacao){
		return repository.findByAnimalNaoAdotado(paginacao);
		
	}
	
	@Transactional 
	public Page<Animal> listAnimaisAdotado (Pageable paginacao){
		return repository.findByAnimalAdotado(paginacao);
		
	}
	
	@Transactional 
	public Page<Animal> listAnimaisTodos(Pageable paginacao){
		return repository.findAll(paginacao); 
	}
  
	public void excluirAnimal(Animal animal) {
		validaAnimal(animal);
		repository.delete(animal);
	}
	
	public void validaAnimal(Animal animal) {
		 
		if (repository.findById(animal.getId()).isPresent() == false) {
			throw new RegraNegocioException("Animal já não existe !");
		}
	}
}
