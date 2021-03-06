package com.animalx.AnimalX.service;
 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional; 
import java.util.stream.Collectors; 
import javax.mail.MessagingException;
import javax.transaction.Transactional;

import com.animalx.AnimalX.constants.LogMessage;
import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.RelatorioDados;
import com.animalx.AnimalX.entity.Usuario;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.repository.AnimalRepository;
import com.animalx.AnimalX.repository.UsuarioRepository;
import com.animalx.AnimalX.utils.EmailSenderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsuarioService {
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private AnimalRepository repositoryAnimal;

 	@Autowired
	private EmailSenderService serviceMail;
 
 	@Autowired
 	private ModelMapper modelMapper;
 	
 	@Value("${url.server}")
 	private String url_server;
 	
 	@Value("${url.front}")
 	private String url_front;
 	
 	@Transactional 
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		if (usuario.getEmail().equals("") || usuario.getEmail().contains("@") == false) {
			log.error(LogMessage.ERRO_AO_SALVAR.toString() +" "+ com.animalx.AnimalX.constants.Classes.USUARIO);
	 		throw new RegraNegocioException("E-mail inv??lido !"); 
		}  
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+ LogMessage.SALVO_COM_SUCESSO.toString());
		BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
		usuario.setSenha(encoderPassword.encode(usuario.getSenha())); 
		return	repository.save(usuario);
	  
	}
	@Transactional 
	public void uploadFotoPerfil(Usuario user){
		Objects.requireNonNull(user.getId());
		
	 	Usuario userUp = new Usuario(); 
		userUp = obterPorId(user.getId()).get();
		userUp.setImg_login(user.getImg_login()); 
		
		log.info(com.animalx.AnimalX.constants.Classes.FOTO +" "+LogMessage.SALVO_COM_SUCESSO.toString());
		repository.save(userUp);
	}
	@Transactional 
	public Usuario updateUsuario(Usuario usuario) {
		
		Objects.requireNonNull(usuario.getId());
		log.warn(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
		if (usuario.getId() == null) {
			log.warn(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
	 		throw new RegraNegocioException("Identificador do usu??rio esta inv??lido !");
		}
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.ALTERADO_COM_SUCESSO.toString());
		BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
		usuario.setSenha(encoderPassword.encode(usuario.getSenha()));
		return	repository.save(usuario);
	  
	}
	@Transactional 
	public void excluirPerfil(Usuario usuario) {
		Objects.requireNonNull(usuario.getId());
		
		 	if (usuario.getId() == null) {
		 		log.error(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
		 		throw new RegraNegocioException("Identificador do usu??rio esta inv??lido !"); 
		 	}
		 	log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.EXCLUIDO_COM_SUCESSO.toString());
			repository.delete(usuario); 
	}
	@Transactional
	public void recuperarSenha(Usuario usuario) {
		
		
		Objects.requireNonNull(usuario.getId());
		if (usuario.getId() == null) {
			log.error(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
			throw new RegraNegocioException("Identificador do usu??rio est?? inv??lido !");
		} 
		
		Usuario usuarioCadastrado = repository.findByEmail(usuario.getEmail());
		
		usuarioCadastrado.setSenha(usuario.getSenha());
		
		BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
		
		usuarioCadastrado.setSenha(encoderPassword.encode(usuario.getSenha()));
	 
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.OPERACAO_REALIZADA_COM_SUCESSO.toString());
		System.out.println("\n SENHA 2 "+usuarioCadastrado.getSenha());
		
		repository.save(usuarioCadastrado);		
	}
	@Transactional 
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
			log.error(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
			 e.printStackTrace();
		}
		log.info("ID CRIADO: "+com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.OPERACAO_REALIZADA_COM_SUCESSO.toString());
		return texto;
	}
	@Transactional 
	public void enviarEmailRecupercaoSenha(Usuario user) throws NoSuchAlgorithmException {
		Usuario usuarioEmail = repository.findByEmail(user.getEmail());
		
		if (usuarioEmail == null) {
			log.info("E-MAIL: "+com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.INFORMACAO_INVALIDA.toString());
			throw new RegraNegocioException("E-mail inv??lido !");
		} 
		
		String textoId = encriptar(usuarioEmail.getId().toString());
		 
		try {
			serviceMail.sendEmailWithAttachment(user.getEmail(),
					url_front+"?id="+textoId+"&email="+usuarioEmail.getEmail(),
			 	 "Recupera????o de senha" );
		} catch (MessagingException e) {
			log.error("E-MAIL: "+com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
			e.printStackTrace();
		}

	}
	 
	public void notificarAdocao(Animal animal) throws NoSuchAlgorithmException {
		Usuario usuarioEmail = repository.findByEmail(animal.getUsuario().getEmail());
		
		if (usuarioEmail == null) {
			log.info("E-MAIL: "+com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.INFORMACAO_INVALIDA.toString());
			throw new RegraNegocioException("E-mail inv??lido !");
		} 
		  
		try {
			DateTimeFormatter formatter =
				    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT ) 
				                     .withZone(ZoneId.of("America/Sao_Paulo"));
			String dataADocao = formatter.format( animal.getData_atualizacao() );
				 
				String textoAdocao = "Animal-X = " +animal.getApelido() +"<br/>"+" Data de ado????o: "+dataADocao.split(" ")[0];
				serviceMail.sendEmailNotificacaoAdocao(animal.getUsuario().getEmail(),
						  textoAdocao,
				 	 "Notifica????o de ado????o" );
		  
			
		} catch (MessagingException e) {
			log.error("E-MAIL: "+com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
			e.printStackTrace();
		} 
	}
	 	
	public void triggerMail(Usuario usuario ) throws MessagingException {

		serviceMail.sendSimpleEmail(usuario.getEmail(),
				"Acesse o link para recuperar sua senha  ",
				"Link:"+url_server+"/api/usuario/rec" );
	}
	 
  
	private Usuario toUsuarioModel(Usuario usuario) {
		usuario.setSenha("");
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.OPERACAO_REALIZADA_COM_SUCESSO.toString());
		return modelMapper.map(usuario,Usuario.class);
		
	}
	@Transactional 
	public List<Usuario> listUsuarios (){
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.OPERACAO_REALIZADA_COM_SUCESSO.toString());
		
		return repository.findAll()
				.stream()
				.map(this::toUsuarioModel)
				.collect(Collectors.toList());
	}

	@Transactional 
	public RelatorioDados getRelatorio() {
		
		 RelatorioDados relatorio = new RelatorioDados(); 
		 relatorio.setQtdUsuariosCadastrados(repository.count());
		 relatorio.setQtdAnimaisCadastrados(repositoryAnimal.count());
		 relatorio.setQtdAnimaisAdotados(repositoryAnimal.qtdAnimaisAdotados());
		
		return relatorio;
	}
	public Optional<Usuario> obterPorId(Long id) { 
		if (id != null) {
			return repository.findById(id);
		}else
			log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.INFORMACAO_INVALIDA.toString());
			return null;
		
	}
	
	public boolean validarEmail(String email) {
		boolean existe =  repository.existsByEmail(email);
		
		if (existe) {
			log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.FALHA_AO_FINALIZAR_OPERACAO.toString());
			throw new RegraNegocioException("J?? existe um usu??rio cadastrado com este email.");
		}
		return existe; 
	} 
	@Transactional 
	public Usuario findByEmail(String email) {
		Objects.requireNonNull(email); 
		log.info(com.animalx.AnimalX.constants.Classes.USUARIO +" "+LogMessage.OPERACAO_REALIZADA_COM_SUCESSO.toString());
		return repository.findByEmail(email);
				
	}
	 
}
