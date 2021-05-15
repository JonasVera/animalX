package com.animalx.AnimalX.security;

import java.util.ArrayList;
import java.util.List;  
import com.animalx.AnimalX.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserFactory {

	private JwtUserFactory () {
		
	}
	
public static JwtUser create(Usuario usuario ) {
 
	return new JwtUser(usuario.getId().toString() ,usuario.getEmail(),usuario.getSenha(), mapToGrantedAuthoriries(usuario.getTipo_usuario()));
}

private static List<GrantedAuthority> mapToGrantedAuthoriries(String profileEnum){
	List<GrantedAuthority> authories = new ArrayList<GrantedAuthority>();
	authories.add(new SimpleGrantedAuthority(profileEnum.toString()));
	
	return authories;
}
}
