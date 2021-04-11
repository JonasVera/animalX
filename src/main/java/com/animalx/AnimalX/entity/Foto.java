package com.animalx.AnimalX.entity;
 
import javax.persistence.*;

 
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity

@Table(name = "fotos", schema = "api_animalx")
public class Foto {
	
	@Id
	@Column(name = "id")	 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "caminho")
	private String caminho;
	@Column(name = "tamanho")
	private String tamanho;
	 
	@Column(name = "contexto")
	private String contexto;

	@ManyToOne
	@JoinColumn(name = "animal_id")
	private Animal animal;
}
