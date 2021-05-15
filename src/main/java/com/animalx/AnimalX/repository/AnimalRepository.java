package com.animalx.AnimalX.repository;

 
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Usuario;
 
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>  {
	
	@Query("SELECT count(a.situacao) FROM  Animal a where a.situacao = 'ADOTADO'")
	Long qtdAnimaisAdotados();
	
	@Query("SELECT a FROM  Animal a where a.situacao != 'ADOTADO'") 
	Page<Animal> findByAnimalNaoAdotado(Pageable paginacao);
	
	@Query("SELECT a FROM  Animal a where a.situacao = 'ADOTADO'") 
	Page<Animal> findByAnimalAdotado(Pageable paginacao);
	
	@Query("SELECT a FROM  Animal a where a.usuario = :usuario") 
	Page<Animal> findByAnimalUsuario(Pageable paginacao,@Param("usuario") Usuario usuario );
	
	
}
