package com.animalx.AnimalX.resource;
import com.animalx.AnimalX.entity.Animal;
import com.animalx.AnimalX.entity.Foto;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.FotoService;
import com.animalx.AnimalX.service.S3StorageService;
import com.animalx.AnimalX.utils.FotoUploadDisco;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/foto")
@CrossOrigin(origins = "*")
public class FotoResource {

	@Autowired
	private FotoService service;
	 
    @Autowired
    private S3StorageService serviceS3;
      
	 	@PostMapping("/upload/disc")
	    public ResponseEntity<?> uploadImagem(@RequestParam MultipartFile file){
	 		  Foto foto = new Foto();
	 		  FotoUploadDisco uploadDisco = new FotoUploadDisco();
	 		   
	    	  foto.setNome(file.getOriginalFilename());
	    	  foto.setContexto("Animal");
	    	  foto.setTamanho(file.getSize());
	    	  uploadDisco.salvarFoto(file);
	    	  try {
	    		  Foto ftSalva =   service.adicioanarFoto(foto);
	  			return new ResponseEntity<Foto>(ftSalva, HttpStatus.CREATED);
	  		}catch (RegraNegocioException e) {
	  			return ResponseEntity.badRequest().body(e.getMessage());
	  		}
	    }
	 	
	 	@PostMapping("/upload/storage/{id_animal}")
	    public ResponseEntity<?> uploadStorage(@PathVariable("id_animal") Long id_animal ,@RequestParam MultipartFile file){
	 		  Foto foto = new Foto();
	 		  Animal animal = new Animal();
	 		  animal.setId(id_animal);
	 		  try {
	 			   
	 		    int qtdFotoAnimal  = service.obterPorAnimal(animal).size();
	 		    if (!file.isEmpty()) {
	 				if(qtdFotoAnimal < 3) {
	 					foto.setContexto("Animal");
	 					foto.setAnimal(animal);
	 					foto.setTamanho(file.getSize());
	 					foto.setData_cadastro(Instant.now());
	 				 	foto.setData_atualizacao(Instant.now());
	 					String animalFoto = serviceS3.uploadFile(file); 
	 					foto.setNome(animalFoto); 
	 		    	    Foto ftSalva =   service.adicioanarFoto(foto);
	 		  			return new ResponseEntity<Foto>(ftSalva, HttpStatus.CREATED);
	 				}else {
	 					return ResponseEntity.badRequest().body("O limite de fotos por animal foi excedido !"); 
	 				}
	 		    }else {
	 		    	return ResponseEntity.badRequest().body("Sem arquivo para enviar"); 
	 		    } 
	  		}catch (RegraNegocioException e) {
	  			return ResponseEntity.badRequest().body(e.getMessage());
	  		}
	    }
		
	 	@DeleteMapping("/excluirFoto/storage/{id_foto}")
		public ResponseEntity<?>  deleteUsuario (@PathVariable("id_foto") Long id_foto  ) {
			 
	 			Foto ftAnimal = new Foto(); 
	 			ftAnimal.setId(id_foto);
	 			
			  if(ftAnimal == null){
					return ResponseEntity.badRequest().body("Foto tem identificador inválido");
				}else{
					return service.obterPorId(ftAnimal.getId()).map(
							  entity ->{
								try { 
									  Foto ftDelete = new Foto();
									  ftDelete.setNome(entity.getNome());
									  ftDelete.setId(ftAnimal.getId()); 
									  service.deletarFoto(ftDelete);
									  if (!ftDelete.getNome().isEmpty())
										  serviceS3.deleteFile(ftDelete.getNome()); 
									  return new ResponseEntity<>(HttpStatus.OK);
								} catch (RegraNegocioException e) {
									return ResponseEntity.badRequest().body(e.getMessage());
								} 
						 
							}).orElseGet(() 
							-> new ResponseEntity<String>("Foto não encontrada.",HttpStatus.BAD_REQUEST));
				}  		 
		}
	 	 
}
