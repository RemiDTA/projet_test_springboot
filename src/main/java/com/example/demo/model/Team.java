package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;

import com.example.demo.validation.EquipeValide;

@Entity
@Table(name = "team")
@EquipeValide // Je souhaite valider le chef d'équipe uniquement, cependant au niveau des annotations j'ai accès à ce que
// j'essaie de valider, si je créer une annotation au niveau du champ, j'aurais le champ (chef d'équipe), mais je n'aurais pas l'objet de l'attribut (l'équipe)
// N'ayant pas ce contexte, je ne sais pas déterminer qui sont les users à partir de l'information du chef d'équipe,
// donc je créer une annotation au niveau de l'objet qui contient le chef d'équipe et les users
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(length = 150)
	private String description;

	@Column(length = 150)
	private String emplacement;

	@Column
	@PastOrPresent // utilisation de spring validator => permet de s'assurer qu'une date est dans le passée
	private LocalDate dateCreation;

	@OneToOne
	@JoinColumn(name = "id_chefEquipe")
	private User chefEquipe;

	@OneToMany(mappedBy = "equipe") // La propriété "equipe" dans la classe User
	// @Cascade(CascadeType.PERSIST) => permet de créer une relation forte entre les users et la team courante déclenchant la sauvegarde des utilisateurs lors de la sauvegarde de l'équipe
	// Cela implique que si l'on récupère un body avec des modifications sur la liste des users, ces modifications seront sauvegardées sur chaque user
	private List<User> users;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public long getId() {
		return this.id;
	}

	public String getEmplacement() {
		return this.emplacement;
	}

	public void setEmplacement(final String emplacement) {
		this.emplacement = emplacement;
	}

	public User getChefEquipe() {
		return this.chefEquipe;
	}

	public void setChefEquipe(final User chefEquipe) {
		this.chefEquipe = chefEquipe;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(final List<User> users) {
		this.users = users;
	}

	public LocalDate getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(final LocalDate dateCreation) {
		this.dateCreation = dateCreation;
	}

}
