package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	EncodeurConfig encodeur;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * Gestion des droits en fonction des roles et des URL aux utilisateurs :
	 * Les users lambda ont le droit en lecture sur les équipes et les users uniquement alors que les admin n'ont pas de restriction
	 *
	 * Nb : /toto/** => signifie toutes les urls sous toto (/toto/truc, /toto/truc/bidule, ...)
	 * /toto/* => signifie toutes les urls DIRECTEMENT sous toto (/toto/truc, /toto/bidule, ...) mais pas /toto/truc/bidule
	 * 
	 * NB 2 : Il faut exclure le /login sinon c'est comme si on demandait d'être authentifié pour s'authentifier
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
	    .authorizeRequests()
	    .antMatchers("/public/**").permitAll()
	    .antMatchers("/admin/**").hasRole("ADMIN")
	    .antMatchers("/login").permitAll() // Exclure la page de connexion de l'authentification
	    .anyRequest().authenticated()
	    .and()
	    .formLogin()
	    .and()
	    .logout().permitAll();
	}

	/**
	 * On utilise notre service spécifique nottament pour les iddentifiants de connexions
	 */
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return this.encodeur.getEncodeur();
	}
}
