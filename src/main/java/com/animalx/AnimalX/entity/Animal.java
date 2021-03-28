package com.animalx.AnimalX.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Table(name = "animal", schema = "api_animalx")
@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
	@Id
	@Column(name = "id")	 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "apelido")
	private String  apelido;

	@Column(name = "especie")
	private String  especie;

	@Column(name = "raca")
	private String  raca;

	@Column(name = "descricao")
	private String  descricao;

	@Column(name = "peso")
	private BigDecimal peso;

	@Column(name = "categoria")
	private String categoria;

	@Column(name = "idade")
	private int idade;

	@Column(name = "situacao")
	private String situacao;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "sexo")
	private String sexo;

	@Column(name = "longitude")
	private String longitude;

	@Column(name = "latitude")
	private String latitude;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "data_cadastro")
	private LocalDate data_cadastro;
	
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "data_atualizacao")
	private  LocalDate data_atualizacao;
	
    @OneToMany(mappedBy = "animal")
    @JsonIgnore
	private List<Foto> fotos;
}
