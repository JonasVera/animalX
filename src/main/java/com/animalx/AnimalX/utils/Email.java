package com.animalx.AnimalX.utils;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class Email {
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	@Bean
	public JavaMailSender getJavaMailSender() {
	    
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("contatosmallmanager@gmail.com");
	    mailSender.setPassword("2071810010");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
	
	public void sendSimpleMessage( String to, String subject, String text) {
		        
		        SimpleMailMessage message = new SimpleMailMessage(); 
		        message.setFrom("contatosmallmanager@gmail.com");
		        message.setTo(to); 
		        message.setSubject(subject); 
		        message.setText(text);
		        mailSender.send(message);
		        
		    }
	
}
