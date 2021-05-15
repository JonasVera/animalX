package com.animalx.AnimalX.repository;

import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Foto; 
import java.util.List;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
	List<Foto> findByAnimal(Animal animal);  
	
}
