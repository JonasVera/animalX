package com.animalx.AnimalX.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
import org.springframework.web.multipart.MultipartFile;

public class FotoUploadDisco {

	 
	private String raiz = "C:\\uploadAnimalX";
	 
	 
	private String diretorioFotos = "\\perfil";
	
	public void salvarFoto(MultipartFile foto) {
		this.salvar(this.diretorioFotos, foto);
	}
	
	public void salvar(String diretorio, MultipartFile arquivo) {
		Path diretorioPath = Paths.get(this.raiz, diretorio);
		Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());
		
		try {
			Files.createDirectories(diretorioPath);
			arquivo.transferTo(arquivoPath.toFile());			
		} catch (IOException e) {
			throw new RuntimeException("Problemas na tentativa de salvar arquivo.", e);
		}		
	}
	
}
