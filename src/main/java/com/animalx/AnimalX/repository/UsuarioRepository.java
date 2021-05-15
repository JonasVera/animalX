package com.animalx.AnimalX.repository;
 
import com.animalx.AnimalX.entity.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	boolean existsByEmail(String email);
	Usuario findByEmail(String email); 	
	  
}
