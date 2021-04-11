package com.animalx.AnimalX.repository;
 
import com.animalx.AnimalX.entity.Contato;
import com.animalx.AnimalX.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
	boolean existsByEmail(String email);
	Usuario findByEmail(String email);
}
