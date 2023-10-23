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
				.cors().and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/user/**", "/team/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN")
				.antMatchers("/login").permitAll() // Exclure la page de connexion de l'authentification
				.anyRequest().authenticated()
				.and().httpBasic()
				.and().formLogin()
				.and().logout().permitAll();
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

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:4200"); // Vous pouvez spécifier ici les origines autorisées
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
