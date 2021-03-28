package com.animalx.AnimalX.repository;

import com.animalx.AnimalX.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>  {
	 
}
