package com.animalx.AnimalX.exeptions;

import java.io.IOException; 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter; 
import com.animalx.AnimalX.utils.JwtTokenUtil; 
public class JwtAutheticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		 
		String authToken = request.getHeader("Authorization"); 
		
		if (authToken != null) {
			authToken = authToken.substring(7, authToken.length()); 
		}
		String userName = jwtTokenUtil.getUserFromToken(authToken);
		
		System.out.println("USUARIO => "+userName);
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() ==null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			if (jwtTokenUtil.valiadeToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authetication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authetication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authetication);
			}
		}
		chain.doFilter(request, response);
	}

}
