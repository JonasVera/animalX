package com.animalx.AnimalX.config;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.animalx.AnimalX.exeptions.JwtAuthenticationEntryPoint;
import com.animalx.AnimalX.exeptions.JwtAutheticationTokenFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthEntryPoint;
	@Autowired
	private UserDetailsService userDetailsService;
	 
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAutheticationTokenFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAutheticationTokenFilter();
	}
	
		@Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	 
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
		}
		
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET,
				"/",
				"/*.html",
				"favicon.ico",
				"/**/*.html",
				"/**/*.css",
				"/**/*.js"
				).permitAll()
				.antMatchers("/api/auth/**").permitAll() 
				.antMatchers("/api/usuario/salvar").permitAll()
				.antMatchers(HttpMethod.GET,"/api/animal/**").permitAll()
				.antMatchers(HttpMethod.GET,"/api/animal/adotados").permitAll()
				.antMatchers("/api/usuario/emailRecuperarSenha/**").permitAll() 
				.antMatchers("/api/usuario/recuperarSenha/**").permitAll() 
				.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
				.antMatchers(HttpMethod.GET, "/actuator/**").hasRole("DMIN")
				.antMatchers(HttpMethod.GET, "/api/usuario/usuarios/").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "api/usuario/relatorio").hasRole("ADMIN")
				  
				.anyRequest().authenticated();
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(),UsernamePasswordAuthenticationFilter.class);
		 
		httpSecurity.headers().cacheControl();
	}
 
}
