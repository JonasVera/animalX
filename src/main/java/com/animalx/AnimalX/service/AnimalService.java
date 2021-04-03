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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
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
		Example<Animal> example = Example.of(animalFiltro, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		 
		return repository.findAll(example);
	}
	
	@Transactional 
	public Optional<Animal> buscarPorId(Animal animal) {
	 
		 if	(animal.getUsuario().getId() != null) {
			 return repository.findById(animal.getId());
		 }else {
			 throw new RegraNegocioException("Empresa não encontrada para este usúario.");
		 } 
	}
 
	public void alterarAnimal(Animal animal) {
		repository.save(animal);
	}
	@Transactional 
	public void listarAnimais() {
		repository.findAll();
	}
	@Transactional 
	public Optional<Animal> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	public void excluirAnimal(Animal animal) {
		repository.delete(animal);
	}
	
	public void validaAnimal(Animal animal) {
		 
		if (repository.findById(animal.getId()).isPresent() == false) {
			throw new RegraNegocioException("Empresa não existe !");
		}
	}
}
