package com.animalx.AnimalX.resource;
 
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
 
import javax.mail.MessagingException;

import com.animalx.AnimalX.constants.ProfileEnum;
import com.animalx.AnimalX.dto.UsuarioStartDTO;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.S3StorageService;
import com.animalx.AnimalX.service.UsuarioService; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuario/")
@CrossOrigin(origins = "*")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	 @Autowired
	 private S3StorageService serviceS3;
	 
	@PostMapping("/salvar") 
	public ResponseEntity<?> salvarUsuario ( @RequestBody UsuarioStartDTO dto) {

		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.data_cadastro((Instant.now()))
				.tipo_usuario(ProfileEnum.ROLE_CUSTUMER.toString())
				.build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("usuarios") 
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> listUsuarios() {
		
		return ResponseEntity.ok(service.listUsuarios());
	}
	 
	@GetMapping("relatorio") 
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> relatorio() {
		
		return ResponseEntity.ok(service.getRelatorio());
	}
	 
	
	@PostMapping("/update")
	public ResponseEntity<?> updateUsuario( @RequestBody UsuarioStartDTO dto) {
		if(dto == null || dto.getId() == null || dto.getId().toString() == ""){
			return ResponseEntity.badRequest().body("Não foi possivel atualizar o usúario");
		}
		else{
			return service.obterPorId(dto.getId()).map(
					  entity ->{
						try { 
							  Usuario usuario = Usuario.builder()
										.id(dto.getId())
										.nome(dto.getNome())
										.email(dto.getEmail())
										.data_cadastro(Instant.now())
										.senha(dto.getSenha())
										.data_atualizacao(Instant.now())
										.tipo_usuario(ProfileEnum.ROLE_CUSTUMER.toString())
										.build(); 
							  entity = usuario;
							  
							  service.updateUsuario(usuario);
							  
							  return ResponseEntity.ok(entity);
						} catch (RegraNegocioException e) {
							return ResponseEntity.badRequest().body(e.getMessage());
						} 
				 
					}).orElseGet(() 
					-> new ResponseEntity<String>("Usuario não encontrado.",HttpStatus.BAD_REQUEST));
		}
	}
	@DeleteMapping("/excluirPerfil")
	public ResponseEntity<?>  deleteUsuario ( @RequestBody UsuarioStartDTO dto) {
		 
		if(dto == null || dto.getId() == null || dto.getId().toString() == ""){
				return ResponseEntity.badRequest().body("Não foi possivel atualizar o usúario");
			}
			else{
				return service.obterPorId(dto.getId()).map(
						  entity ->{
							try { 
								  Usuario usuario = Usuario.builder()
											.id(dto.getId())
											.build();   
								  entity = usuario; 
								  service.excluirPerfil(usuario); 
								  return new ResponseEntity<>(HttpStatus.OK);
							} catch (RegraNegocioException e) {
								return ResponseEntity.badRequest().body(e.getMessage());
							} 
					 
						}).orElseGet(() 
						-> new ResponseEntity<String>("Usuario não encontrado.",HttpStatus.BAD_REQUEST));
			}  		 
	}
	 
	@PostMapping("emailRecuperarSenha/{email}")
	public ResponseEntity<?>  emailRecupersenha (@PathVariable("email") String email ) throws MessagingException {
		Usuario user = new Usuario();
		user.setEmail(email);
	 
		if (user.getEmail().equals("") || user.getEmail().contains("@")== false)
			return	new ResponseEntity<String>("E-mail invalido !",HttpStatus.BAD_REQUEST);
		else {
			
			try {
				service.enviarEmailRecupercaoSenha(user);
			} catch (NoSuchAlgorithmException e) {
			  e.printStackTrace();
			} 
			return new ResponseEntity<Usuario>(HttpStatus.CREATED);	 
		} 
	}
	
	@PostMapping("recuperarSenha/{id}/{email}")
	public ResponseEntity<?>  atualizarSenha (@PathVariable("id") String id,@PathVariable("email") String email,@RequestBody UsuarioStartDTO dto){
		Usuario user = new Usuario();
		
		user.setEmail(email);
		user.setSenha(dto.getSenha());
		if (user.getEmail().equals("") || user.getEmail().contains("@") == false)
			return	new ResponseEntity<String>("E-mail invalido !",HttpStatus.BAD_REQUEST);
		
		user = service.findByEmail(user.getEmail());
		
		if (user.getEmail() == null || user.getEmail().equals("@")) 
			return	new ResponseEntity<String>("E-mail invalido !",HttpStatus.BAD_REQUEST);
		
		String encript = service.encriptar(user.getId().toString());
		
		if(encript.equals(id)) { 
		 service.recuperarSenha(user);
			 	
		}else {
			return  new ResponseEntity<String>("Usuario não encontrado.",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Usuario>(HttpStatus.OK);	 
	}
	 
	@PostMapping("/uploadFotoPerfil/{id_usuario}")
	public ResponseEntity<?> uploadFotoPerfilUsuario(@PathVariable("id_usuario") Long id_usuario,@RequestParam MultipartFile file){
	 
		 Usuario userUpload = new Usuario();
		 userUpload.setId(id_usuario);
		 
	 	 if(service.obterPorId(userUpload.getId()).isPresent()) {
	 		 userUpload.setImg_login(serviceS3.uploadFile(file));
	 		  service.uploadFotoPerfil(userUpload);
	 	 }else
	 		return  new ResponseEntity<String>("Foto não encontrada.",HttpStatus.BAD_REQUEST);
	 	
		try {
			return new ResponseEntity<Usuario>(userUpload, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
