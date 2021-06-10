package com.animalx.AnimalX.entity;
 
import java.time.Instant;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity 
@Table(name = "foto", schema = "api_animalx")
public class Foto {
	
	@Id
	@Column(name = "id")	 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nome")
	private String nome;
	 
	@Column(name = "tamanho")
	private Long tamanho;
	 
	@Column(name = "contexto")
	private String contexto;

	@ManyToOne
	@JoinColumn(name = "id_animal")
	@JsonIgnore
	private Animal animal;
	
	@Column(name = "data_cadastro")
	private Instant data_cadastro;
 
	@Column(name = "data_atualizacao")
	private Instant data_atualizacao;
	
	 
}
