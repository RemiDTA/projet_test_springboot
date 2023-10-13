package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Création d'une classe permettant d'être injecté partout où il faut utiliser de l'encodage
 * Cette classe n'est pas obligatoire, plusieurs instances de BCryptPasswordEncoder peuvent exister au sein d'un programme et l'encodage se passerait bien à priori
 * Mais d'un point de vue performance cela est souhaitable de n'utiliser qu'une instance de BCryptPasswordEncoder (ou tout autre encodeur)
 *
 * @author ferre
 *
 */
@Configuration
public class EncodeurConfig {

	private BCryptPasswordEncoder encodeur;

	public BCryptPasswordEncoder getEncodeur() {
		return this.encodeur;
	}

	public void setEncodeur(final BCryptPasswordEncoder encodeur) {
		this.encodeur = encodeur;
	}

	EncodeurConfig() {
		super();
		this.encodeur = new BCryptPasswordEncoder();

	}
}
