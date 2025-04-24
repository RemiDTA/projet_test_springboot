package com.example.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "utilisateur")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(length = 150)
	private String nom;

	@Column(length = 100)
	private String prenom;

	@Pattern(regexp = "^(06|07)\\d{8}$", message = "Le numéro de téléphone doit commencer par 06 ou 07 et être suivi de 8 chiffres.")
	@Column(length = 10)
	private String telephone;

	@JsonIgnore // Si les User affichent les Team et que les Team affichent les users,
	// on a une boucle infinie
	@ManyToOne
	@JoinColumn(name = "id_equipe") // id_equipe => champ BDD
	private Team equipe;

	@JsonIgnore // Si les User affichent les Team et que les Team affichent les users,
	// on a une boucle infinie
	@OneToMany(mappedBy = "chefEquipe")
	private List<Team> equipeResponsable;

	@ManyToMany(mappedBy = "collaborateurs")
	private List<Projet> projets;

	@Email
	@Column(length = 300, unique = true)
	@NotEmpty
	private String email;

	@Column(length = 300)
	@NotEmpty
	@JsonIgnore
	private String motPasse;

	public String getMotPasse() {
		return this.motPasse;
	}

	public void setMotPasse(final String motPasse) {
		this.motPasse = motPasse;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public List<Projet> getProjets() {
		return this.projets;
	}

	public void setProjets(final List<Projet> projets) {
		this.projets = projets;
	}

	public Long getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	public Team getEquipe() {
		return this.equipe;
	}

	public void setEquipe(final Team equipe) {
		this.equipe = equipe;
	}

	public List<Team> getEquipeResponsable() {
		return this.equipeResponsable;
	}

	public void setEquipeResponsable(final List<Team> equipeResponsable) {
		this.equipeResponsable = equipeResponsable;
	}

}
