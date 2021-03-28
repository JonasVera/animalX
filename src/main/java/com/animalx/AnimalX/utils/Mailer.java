package com.animalx.AnimalX.utils;
 
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
@Component
public class Mailer {
	 
	private JavaMailSender javaMailSender = new JavaMailSenderImpl();
	
	public void enviar(Mensagem mensagem) {
		
		 SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setFrom("contatosmallmanager@gmail.com");
		simpleMailMessage.setTo(mensagem.getDestinatarios()
				.toArray(new String[mensagem.getDestinatarios().size()]));
		simpleMailMessage.setSubject(mensagem.getAssunto());
		simpleMailMessage.setText(mensagem.getCorpo());
		
		javaMailSender.send(simpleMailMessage);
	}
}
