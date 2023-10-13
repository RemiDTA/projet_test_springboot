package com.example.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "projet")
public class Projet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 150)
	private String entreprise;

	@Column(length = 150)
	private String nomProjet;

	@JsonIgnore // Relation n,n, il y a une URL spécifique pour afficher les users appartenant à un projet et les projets de l'utilisateur
	@ManyToMany
	@JoinTable(name = "projet_collabo") // Nom de la table de jointure en BDD
	private List<User> collaborateurs;

	public Long getId() {
		return this.id;
	}

	public String getEntreprise() {
		return this.entreprise;
	}

	public void setEntreprise(final String entreprise) {
		this.entreprise = entreprise;
	}

	public String getNomProjet() {
		return this.nomProjet;
	}

	public void setNomProjet(final String nomProjet) {
		this.nomProjet = nomProjet;
	}

	public List<User> getCollaborateurs() {
		return this.collaborateurs;
	}

	public void setCollaborateurs(final List<User> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}

}
