package com.animalx.AnimalX.resource;


import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import javax.mail.MessagingException;
import com.animalx.AnimalX.dto.UsuarioStartDTO;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.UsuarioService;
import com.animalx.AnimalX.utils.FotoUploadDisco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuario/")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@PostMapping("/salvar")
	public ResponseEntity<?> salvarUsuario ( @RequestBody UsuarioStartDTO dto) {

		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.data_cadastro((Instant.now()))
				.tipo_usuario("user")
				.build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("usuarios") 
	public ResponseEntity<?> listUsuarios() {
		
		return ResponseEntity.ok(service.listUsuarios());
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<?> buscar(
								 @RequestParam(value = "nome" ,required = false) String nome,
			 					 @RequestParam(value = "email",required = false) String email,
			 					 @RequestParam(value = "tipo",required = false) String tipo
			 					 
				) {
		 
		Usuario usuarioFiltro = new Usuario();
			usuarioFiltro.setNome(nome);
			usuarioFiltro.setNome(email);
			usuarioFiltro.setTipo_usuario(tipo);
		List<Usuario> usuarioEncontrados = service.buscar(usuarioFiltro);
		return ResponseEntity.ok(usuarioEncontrados);
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateUsuario( @RequestBody UsuarioStartDTO dto) {
		if(dto == null){
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
										.data_atualizacao(Instant.now())
										.tipo_usuario("user")
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
		 
		  if(dto == null){
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
	 
		try {
			service.enviarEmailRecupercaoSenha(user);
		} catch (NoSuchAlgorithmException e) {
		  e.printStackTrace();
		} 
		return new ResponseEntity<Usuario>(HttpStatus.CREATED);	 
	}
	
	@PostMapping("RecuperarSenha/{id}/{email}")
	public ResponseEntity<?>  atualizarSenha (@PathVariable("id") String id,@PathVariable("email") String email,@RequestBody UsuarioStartDTO dto){
		Usuario user = new Usuario();
		
		user.setEmail(email);
		user = service.findByEmail(user.getEmail());
		String encript = service.encriptar(user.getId().toString());
		user.setSenha(dto.getSenha());
		if(encript.equals(id)) {

		 service.recuperarSenha(user);
			 	
		}else {
			return  new ResponseEntity<String>("Usuario não encontrado.",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Usuario>(HttpStatus.OK);	 
	}
	 
	@PostMapping("/uploadFotoPerfil/{id_usuario}")
	public ResponseEntity<?> uploadFotoPerfilUsuario(@PathVariable("id_usuario") Long id_usuario,@RequestParam MultipartFile file){
		 FotoUploadDisco uploadDisco = new FotoUploadDisco();
		 Usuario userUpload = new Usuario();
		 userUpload.setId(id_usuario);
		  //RENOMEAR IMAGEM 
		  
		 userUpload.setImg_login(file.getOriginalFilename());
	 	 uploadDisco.salvarFoto(file);
	 	 service.uploadFotoPerfil(userUpload);
		try {
			return new ResponseEntity<Usuario>(userUpload, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
