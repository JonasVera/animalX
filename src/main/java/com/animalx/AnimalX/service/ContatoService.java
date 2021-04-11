package com.animalx.AnimalX.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Contato;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
  
@Service
public class ContatoService {

	@Autowired
	private com.animalx.AnimalX.repository.ContatoRepository repository;
	@Autowired
	private AnimalService animalService;
	 
	@Transactional 
	public Contato cadastrarContatoComUsuario(Contato contato) {
		
		Optional<Animal> animal = animalService.obterPorId(contato.getAnimal().getId());

		if (animal.isPresent()) {
			 return repository.save(contato);
		} else {
			throw new RegraNegocioException("Não foi possivel cadastrar o contato, animal não encontrado.");
		}

	}
	 
	@Transactional 
	public List<Contato> buscar(Contato contatoFiltro) {
		Example<Contato> example = Example.of(contatoFiltro, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		 
		return repository.findAll(example);
	}
	
	@Transactional 
	public Optional<Contato> buscarPorId(Contato contato) {
	    return repository.findById(contato.getId());
		  
	}
 
	public void alterarContato(Contato contato) {
		repository.save(contato);
	}
	@Transactional 
	public void listarAnimais() {
		repository.findAll();
	}
	
	@Transactional 
	public Optional<Contato> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	public void excluirContato(Contato contato) {
		repository.delete(contato);
	}
	
	public void validaContato(Contato contato) {
		 
		if (repository.findById(contato.getId()).isPresent() == false) {
			throw new RegraNegocioException("Contato já não existe !");
		}
	}
}
