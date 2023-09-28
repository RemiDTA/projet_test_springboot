package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "team")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 150)
	private String description;

	@Column(length = 150)
	private String emplacement;

	@OneToOne
	@JoinColumn(name = "id_chefEquipe")
	private User chefEquipe;

	@OneToMany(mappedBy = "equipe") // La propriété "equipe" dans la classe User
	private List<User> users;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
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

}
