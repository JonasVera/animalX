package com.animalx.AnimalX.service;
 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.repository.UsuarioRepository;
import com.animalx.AnimalX.utils.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

 	@Autowired
	private EmailSenderService serviceMail;
 
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return	repository.save(usuario);
	  
	}
	public void uploadFotoPerfil(Usuario user){
	 	Usuario userUp = new Usuario(); 
		userUp = obterPorId(user.getId()).get();
		userUp.setImg_login(user.getImg_login()); 
		repository.save(userUp);
	}
	public Usuario updateUsuario(Usuario usuario) {
		
		Objects.requireNonNull(usuario.getId());
		 
		return	repository.save(usuario);
	  
	}
	
	public void excluirPerfil(Usuario usuario) {
		Objects.requireNonNull(usuario.getId());
		repository.delete(usuario);
		
	}
	
	public void recuperarSenha(Usuario usuario) {
		Objects.requireNonNull(usuario.getId());
		Usuario usuarioCadastrado = repository.findByEmail(usuario.getEmail());
		usuarioCadastrado.setSenha(usuario.getSenha());
		
		repository.save(usuarioCadastrado);		
	}
	
	public  String encriptar (String texto) {
		 
		try {
			Date data = new Date();
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
  
			formatador.format( data );
			
			texto = texto + formatador.format(data).toString();
			
			MessageDigest md = null;
			
			md = MessageDigest.getInstance("MD5");
			
			BigInteger hash = new BigInteger(1,md.digest(texto.getBytes()));
			
			texto =  hash.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();
		}
	
		System.out.println("\nPRINT = "+texto);
		return texto;
	}
	
	public void enviarEmailRecupercaoSenha(Usuario user) throws NoSuchAlgorithmException {
		Usuario usuarioEmail = repository.findByEmail(user.getEmail());
		
		String textoId = encriptar(usuarioEmail.getId().toString());
		
		  serviceMail.sendSimpleEmail(user.getEmail(),
			 	 "Acesse o link para recuperar sua senha\nLink: https://localhost/api/usuario/"+textoId+"/"+usuarioEmail.getEmail(),
		 	 "Recuperação de senha" );
	}
	 	
	public void triggerMail(Usuario usuario ) throws MessagingException {

		serviceMail.sendSimpleEmail(usuario.getEmail(),
				"Acesse o link para recuperar sua senha  ",
				"Link: https://localhost/api/usuario/rec" );
	}
	 
	@Transactional 
	public List<Usuario> buscar(Usuario usuarioFiltro) {
		Example<Usuario> example = Example.of(usuarioFiltro, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		 
		return repository.findAll(example);
	}
	 
	public List<Usuario> listUsuarios (){
		return repository.findAll();
	}

	
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	public boolean validarEmail(String email) {
		boolean existe =  repository.existsByEmail(email);
		
		if (existe)
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		return existe; 
	} 
	public Usuario findByEmail(String email) {
 
		return repository.findByEmail(email);
				
	}
	 
}
