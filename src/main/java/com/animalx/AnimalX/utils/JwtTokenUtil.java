package com.animalx.AnimalX.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.animalx.AnimalX.security.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtTokenUtil implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private final String CLAIN_KEY_USERNAME = "sub";
	private final String CLAIN_KEY_CREATED = "created";
	private final String CLAIN_KEY_EXPIRED = "exp";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	
	 public String getUserFromToken(String token) {
		 String userName; 
		 try { 
			 final Claims clains = getClaimsFromToken(token);
			 userName = clains.getSubject();
		 }catch (Exception e) {
			userName = null;
		}
		return userName;
	 }
 
	 public Date getExpirationFromToken(String token) {
		 Date expiration;
		 try {
			 final Claims clains = getClaimsFromToken(token);
			 expiration = clains.getExpiration();
		 }catch (Exception e) {
			 expiration = null;
		}
		return expiration;
	 }
	 
	 public Claims getClaimsFromToken(String token) {
		 Claims claims = null;
		 try {
			 claims = (Claims) Jwts.parser()
					 .setSigningKey(secret)
					 .parse(token)
					 .getBody();
			 
		 }catch (Exception e) {
			 expiration = null;
		}
		 return claims;
	 }
	 
	 private Boolean isTokenExpired(String token) {
		 final Date expiration = getExpirationFromToken(token);
		 return expiration.before(new Date());
	 }
	 
	 public String generateToken(UserDetails userDetails) {
		 Map<String, Object> claims = new HashMap<>();
		 claims.put(CLAIN_KEY_USERNAME, userDetails.getUsername());
		 final Date createdDate = new Date();
		 claims.put(CLAIN_KEY_CREATED, createdDate);
		 
		return doGenerateToken(claims);
		 
	 }
	 
	 private String doGenerateToken( Map<String, Object> claims) {
		 final Date createdDate =  (Date) claims.get(CLAIN_KEY_CREATED);
	 
		 final Date expirationDate =  new Date(createdDate.getTime() + 604800 * 1000);
		
		 return Jwts.builder()
				 .setClaims(claims)
				 .setExpiration(expirationDate)
				  .signWith(SignatureAlgorithm.HS512, secret)
				 .compact();
		 
	 }
	 
	 public Boolean canTokenBeRefreshed(String token) {
		 return (!isTokenExpired(token));
	 }
	 
	 public String refreshToken(String token) {
		 String refreshedToken;
		 try {
			 final Claims claims = getClaimsFromToken(token);
			 claims.put(CLAIN_KEY_CREATED,new Date());
			 refreshedToken = doGenerateToken(claims);
		 }catch (Exception e) {
			 refreshedToken = null;
		}
		 return refreshedToken;
	 }
	 
	 public Boolean valiadeToken(String token, UserDetails userDetails) {
		 JwtUser user = (JwtUser) userDetails;
		 final String userName = getUserFromToken(token);
		 return (userName.equals(user.getUsername()) && !isTokenExpired(token));
	 }
	 
}
