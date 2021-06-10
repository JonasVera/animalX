package com.animalx.AnimalX.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
import lombok.ToString;
//import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Entity
@Table(name = "usuario", schema = "api_animalx")
@Data
@Builder 
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
 
	@Id
	@Column(name = "id")	 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome; 

	@Column(name = "img_login")
	private String img_login;

	@Column(name = "email")
	private String  email;

	@Column(name = "whatsapp")
	private String  whatsapp; 
	
	@Column(name = "cidade")
	private String  cidade;
	
	@Column(name = "estado")
	private String  estado;
	 
	@Column(name = "tipo_usuario")
	private String  tipo_usuario;
	 
	@Column(name = "data_cadastro")
	private Instant data_cadastro;
 
	@Column(name = "data_atualizacao")
	private Instant data_atualizacao;
	
	@Column(name = "senha")
	private String senha;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Animal> animal;
	
}
