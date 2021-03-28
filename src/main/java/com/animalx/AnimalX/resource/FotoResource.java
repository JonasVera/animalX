package com.animalx.AnimalX.resource;

import com.animalx.AnimalX.entity.Foto;
import com.animalx.AnimalX.exeptions.RegraNegocioException;
import com.animalx.AnimalX.service.FotoService;
import com.animalx.AnimalX.utils.FotoUploadDisco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/foto")
public class FotoResource {

	@Autowired
	private FotoService service;
	  
	 	@PostMapping
	    public ResponseEntity<?> registrarReceita(@RequestParam MultipartFile file){
	 		  Foto foto = new Foto();
	 		 FotoUploadDisco uploadDisco = new FotoUploadDisco();
	 		  
	    	  String tma = Long.toString(file.getSize());
	    	  foto.setTamanho(tma);
	    	  foto.setNome(file.getOriginalFilename());
	    	  foto.setContexto("Ani");
	    	  uploadDisco.salvarFoto(file);
	    	  try {
	    		  Foto ftSalva =   service.adicioanarFoto(foto);
	  			return new ResponseEntity<Foto>(ftSalva, HttpStatus.CREATED);
	  		}catch (RegraNegocioException e) {
	  			return ResponseEntity.badRequest().body(e.getMessage());
	  		}
	    }

	@PostMapping("/uploadFotoPerfil/{id_usuario}")
	public ResponseEntity<?> uploadFotoPerfilUsuario(@RequestParam MultipartFile file){
		Foto foto = new Foto();
		FotoUploadDisco uploadDisco = new FotoUploadDisco();

		String tma = Long.toString(file.getSize());
		foto.setTamanho(tma);
		foto.setNome(file.getOriginalFilename());
		foto.setContexto("Perfil");
		uploadDisco.salvarFoto(file);
		try {
			Foto ftSalva =   service.adicioanarFoto(foto);
			return new ResponseEntity<Foto>(ftSalva, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
