package com.animalx.AnimalX.entity;
 
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table; 
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters; 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "contato_animal", schema = "api_animalx")
@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contato {
	
	@Id
	@Column(name = "id")	 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "animal")
	private Animal animal;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefone")
	private String tefone;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "cidade")
	private String cidade;
	
	@Column(name = "hash_animal")
	private String hashAnimal;
	
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "data_cadastro")
	private LocalDate data_cadastro;
	
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "data_atualizacao")
	private  LocalDate data_atualizacao;
	
}
