package com.example.demo.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
				// csrf est une protection qui doit être desactivée pour faire fonctionner les appels POSTMAN
				.csrf().disable()
				.cors().and() // La configuration est faite dans corsConfigurationSource
				.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll() // A ne pas faire pour de vrai, mais permet d'accéder à la BDD sans se loguer
				.antMatchers(HttpMethod.GET, "/user/**", "/team/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN")
				.antMatchers("/login").permitAll() // Exclure la page de connexion de l'authentification
				.anyRequest().authenticated() // Oblige l'utilisateur à se connecter
				.and().httpBasic()
				.and().formLogin()
				.and().logout().permitAll()
				.and().headers().frameOptions().disable();				// Utilisé pour H2 (la sécurité avant tout)
	}

	/**
	 * On utilise notre service spécifique nottament pour les iddentifiants de connexions
	 */
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(this.userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return this.encodeur.getEncodeur();
	}

	/**
	 * Configuration sécurité CORS, il s'agit d'une sécurité qui intervient lorsqu'une URL externe au server requête sur le BO
	 * Ce qui est le cas lorsque l'on a un BO et un Front en localhost sur 2 port différent
	 *
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:4200"); // Autorisé mon front (qui utilise le port 4200) a appelé ce back
		configuration.setAllowedMethods(Arrays.asList("*")); // Autorisé toutes les requêtes HTTP

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
