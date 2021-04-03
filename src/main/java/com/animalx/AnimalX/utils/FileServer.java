package com.animalx.AnimalX.utils;

import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileServer {
	@Autowired
	private HttpServletRequest request;
	
	public String write(MultipartFile file) {
		String realPath = request.getServletContext().getRealPath("\\");
	 
		if(file != null) {
			try {
			 
				String path = realPath+"\\"+file.getOriginalFilename();
				System.out.println(path);
				byte[] bytes = file.getBytes();
				Path path1 = Paths.get(path);
				
				Files.write(path1, bytes); 
			
			} catch (IOException e) { 
				e.printStackTrace();
			}
			return file.getOriginalFilename();  
		}else {
			return "";
		}
		 
 }
 
}
