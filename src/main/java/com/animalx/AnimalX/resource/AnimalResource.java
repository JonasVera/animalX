package com.animalx.AnimalX.resource; 
import java.math.BigDecimal;
import java.time.Instant; 
import java.util.List;
import java.util.Optional; 
import javax.mail.MessagingException; 
import com.animalx.AnimalX.dto.AnimalDTO; 
import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.AnimalService;
import com.animalx.AnimalX.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
@RestController
@RequestMapping("api/animal")
@CrossOrigin(origins = "*")
public class AnimalResource {

	@Autowired
	private AnimalService service;
	@Autowired
	private UsuarioService userService;
	 
	@PostMapping("salvar")
	public ResponseEntity<?> adicionarAnimalComUsuario ( @RequestBody AnimalDTO dto) {
		
		Usuario usuario = new Usuario();
		usuario.setId(dto.getUsuario());
		
		Animal animal = Animal.builder()
				.apelido(dto.getApelido())
				.categoria(dto.getCategoria())
				.especie(dto.getEspecie())
				.raca(dto.getRaca())
				.descricao(dto.getDescricao())
				.idade(Integer.parseInt(dto.getIdade()))
				.peso(BigDecimal.valueOf(Double.parseDouble(dto.getPeso())))
				.alura(BigDecimal.valueOf(Double.parseDouble(dto.getAltura())))
				.categoria(dto.getCategoria())
				.situacao(dto.getSituacao())
				.estado(dto.getEstado())
				.cidade(dto.getCidade())
				.data_atualizacao(Instant.now()) 
				.data_cadastro((Instant.now()))
				.sexo(dto.getSexo())
				.usuario(usuario)
				.build();
		try {
			Animal animalSalva = service.cadastrarAnimalComUsuario(animal);
			return new ResponseEntity<Animal>(animalSalva, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("update")
	public ResponseEntity<?> alterarAnimal ( @RequestBody AnimalDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setId(dto.getUsuario());
		 if(dto == null) {
			 return ResponseEntity.badRequest().body("Não foi possivel atualizar o pet informado !");
		 } else {
			
		return service.obterPorId(dto.getId()).map(
				
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
								.estado(dto.getEstado())
								.cidade(dto.getCidade())
								.data_atualizacao(Instant.now()) 
								.usuario(usuario)
								.build();
						  
						  entity = animal;
						  service.alterarAnimal(animal);
						  
						  return ResponseEntity.ok(entity);
					} catch (RegraNegocioException e ) {
						return ResponseEntity.badRequest().body(e.getMessage());
					} 
				}
			).orElseGet(() 
		-> new ResponseEntity<String>("Usúario não encontrado.",HttpStatus.BAD_REQUEST));
		 }		 
	}
	
	@GetMapping("animaisAdocao") 
	public Page<Animal> listAnimais(@PageableDefault(sort = "id", direction = Direction.ASC) Pageable paginacao ) { 
		return service.listAnimais(paginacao); 
	}
	
	@GetMapping("animais/adotados")  
	public Page<Animal> listAnimaisAdotados(@PageableDefault(sort = "id", direction = Direction.ASC) Pageable paginacao ) { 
		return service.listAnimaisAdotado(paginacao); 
	}
	@PutMapping("adotar/{id_animal}")
	public ResponseEntity<?>  adotar (@PathVariable("id_animal") Long id_animal ) throws MessagingException {
		Animal animal = new Animal();
		animal.setId(id_animal);
		Optional<Animal> animalAdotado = service.obterPorId(id_animal);
		
		if (animalAdotado.isPresent()) {
			service.adotar(id_animal);
			return new ResponseEntity<Usuario>(HttpStatus.OK);	
		}else {
			return new ResponseEntity<String>("Animal não encontrado.",HttpStatus.BAD_REQUEST);
		} 
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<?> buscar(
								 @RequestParam(value = "categoria" ,required = false) String categoria,
			 					 @RequestParam(value = "especie",required = false) String especie,
			 					 @RequestParam(value = "raca",required = false) String raca 
				) {

			Animal animalFiltro = Animal.builder()

				.categoria(categoria)
				.especie(especie)
				.raca(raca)
				.build(); 
		 List<Animal> animais = service.buscar(animalFiltro); 
		return ResponseEntity.ok(animais);
		
	}

	@DeleteMapping("/excluirAnimal")
	public ResponseEntity<?>  deleteAnimal ( @RequestBody AnimalDTO dto) {
		 
		  if(dto == null){
				return ResponseEntity.badRequest().body("Não foi possivel atualizar o usúario");
			}
			else{
				return service.obterPorId(dto.getId()).map(
						  entity ->{
							try { 
								  Animal animal = Animal.builder()
											.id(dto.getId())
											.build();  
											 
								  entity = animal;
								  
								  service.excluirAnimal(animal);
								  
								  return new ResponseEntity<>(HttpStatus.OK);
							} catch (RegraNegocioException e) {
								return ResponseEntity.badRequest().body(e.getMessage());
							} 
					 
						}).orElseGet(() 
						-> new ResponseEntity<String>("Animal não encontrado.",HttpStatus.BAD_REQUEST));
			}  		 
	}
	
	@GetMapping("/buscarAnimal/{id_usuario}/{id_animal}")
	public ResponseEntity<?> buscar( @PathVariable( "id_usuario") Long idUsuario, @PathVariable( "id_animal") Long idAnimal) {

		 Animal aniFiltro = new Animal();

		 Optional<Usuario> user = userService.obterPorId(idUsuario);

		 if(!user.isPresent())
			 return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. usuário não encontrado.");
		 else {
			 aniFiltro.setId(idAnimal);
				aniFiltro.setUsuario(user.get());
				 Optional<Animal> animal = service.buscarPorId(aniFiltro);
				 return ResponseEntity.ok(animal.get());
		 }
	}
	 
	 
}
