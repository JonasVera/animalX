package com.animalx.AnimalX.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class MailConfig {
	@Value("${spring.mail.username}")
	private String userName;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private int port;
	
	
		@Bean
		public JavaMailSender mailSender() { 
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			
			mailSender.setHost(host);
			mailSender.setPort(port);
			 
			System.out.println("PRINT "+userName);
			mailSender.setUsername(userName);
			mailSender.setPassword(password);
			
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.starttls.enable", true);
			props.put("mail.smtp.connectiontimeout", 10000);
			
			mailSender.setJavaMailProperties(props);
	 
			return mailSender;
		}
	}