package com.animalx.AnimalX.resource;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.animalx.AnimalX.dto.AnimalDTO;
import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.AnimalService;
import com.animalx.AnimalX.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/animal")
public class AnimalResource {

	@Autowired
	private AnimalService service;
	@Autowired
	private UsuarioService userService;
	 
	@PostMapping
	public ResponseEntity<?> adicionarAnimal ( @RequestBody AnimalDTO dto) {
		
		Usuario usuario = new Usuario();
		usuario.setId(Long.parseLong(dto.getUsuario()));
		
		Animal animal = Animal.builder()
				.apelido(dto.getApelido())
				.categoria(dto.getCategoria())
				.especie(dto.getEspecie())
				.raca(dto.getRaca())
				.descricao(dto.getDescricao())
				.idade(Integer.parseInt(dto.getIdade()))
				.peso(BigDecimal.valueOf(Double.parseDouble(dto.getPeso())))
				.categoria(dto.getCategoria())
				.situacao(dto.getSituacao())
				.longitude(dto.getLogintude())
				.latitude(dto.getLatitude())
				.data_atualizacao(LocalDate.now())
				.data_cadastro(LocalDate.now())
				.sexo(dto.getSexo())
				.build();
		try {
			Animal animalSalva = service.cadastrarEmpresa(animal,usuario);
			return new ResponseEntity<Animal>(animalSalva, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping
	public ResponseEntity<?> alterarAnimal ( @RequestBody AnimalDTO dto) {
		 if(dto == null) 
				return ResponseEntity.badRequest().body("Não foi possivel atualizar a empresa");
		else
			 
		return service.obterPorId(Long.parseLong(dto.getId())).map(
				  entity ->{
					try {

						Animal animal = Animal.builder()
								.apelido(dto.getApelido())
								.categoria(dto.getCategoria())
								.especie(dto.getEspecie())
								.raca(dto.getRaca())
								.descricao(dto.getDescricao())
								.idade(Integer.parseInt(dto.getIdade()))
								.peso(BigDecimal.valueOf(Double.parseDouble(dto.getPeso())))
								.categoria(dto.getCategoria())
								.situacao(dto.getSituacao())
								.longitude(dto.getLogintude())
								.latitude(dto.getLatitude())
								.data_atualizacao(LocalDate.now())
								.data_cadastro(LocalDate.now())
								.build();
						  
						  entity = animal;
						  
						  service.alterarEmpresa(animal);
						  
						  return ResponseEntity.ok(entity);
					} catch (RegraNegocioException e ) {
						return ResponseEntity.badRequest().body(e.getMessage());
					} 
			 
				}).orElseGet(() 
				-> new ResponseEntity<String>("Usúario não encontrado.",HttpStatus.BAD_REQUEST));
			 
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<?> buscar(
								 @RequestParam(value = "categoria" ,required = false) String categoria,
			 					 @RequestParam(value = "especie",required = false) String especie,
			 					 @RequestParam(value = "raca",required = false) String raca,
			 					 @RequestParam( "usuario") Long idUsuario
			 					 
				) {

			Animal animalFiltro = Animal.builder()

				.categoria(categoria)
				.especie(especie)
				.raca(raca)
				.build();
		  
		 Optional<Usuario> user = userService.obterPorId(idUsuario);
		 
		 if(!user.isPresent())
			 return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. animal não encontrado.");
		 else
			 animalFiltro.setUsuario(user.get());
		 	
		 List<Animal> animais = service.buscar(animalFiltro);
		 
		 
		return ResponseEntity.ok(animais);
		
	}

	@GetMapping("/buscar/{id_usuario}/{id_animal}")
	public ResponseEntity<?> buscar( @PathVariable( "id_usuario") Long idUsuario, @PathVariable( "id_animal") Long idAnimal) {

		 Animal aniFiltro = new Animal();

		 Optional<Usuario> user = userService.obterPorId(idUsuario);

		 if(!user.isPresent())
			 return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. usuário não encontrado.");
		 else
			 aniFiltro.setId(idAnimal);
		aniFiltro.setUsuario(user.get());

		 Optional<Animal> animal = service.buscarPorId(aniFiltro);

		return ResponseEntity.ok(animal.get());

	}
	 
}
