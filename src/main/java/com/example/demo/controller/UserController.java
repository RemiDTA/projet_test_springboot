package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Team;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService us;

	@PostMapping
	public void creerUser(@RequestBody final User utilisateur) {
		this.us.creerUser(utilisateur);
	}

	/**
	 * @param utilisateur
	 * @param id          PathVariable pour respecter les bonnes pratiques REST, néanmoins l'ID étant présent dans le body et n'est pas utilisé par le service
	 */
	@PatchMapping("/{id}")
	public void majUser(@RequestBody final User utilisateur, @PathVariable final long id) {
		this.us.majUser(utilisateur);
	}

	@GetMapping
	public List<User> listerUsers() {
		return this.us.recupererTousUser();
	}

	@GetMapping("/{id}")
	public Optional<User> recupererUserById(@PathVariable final Long id) {
		return this.us.recupererUserParId(id);
	}

	@DeleteMapping("/{id}")
	public void supprimerUser(@PathVariable final Long id) {
		this.us.supprimerUser(id);
	}

	@PatchMapping("associer_equipe/{id}")
	public void ajouterUtilisateur(@RequestBody final Team equipe, @PathVariable final long id) {
		this.us.ajouterUtilisateur(equipe, id);
	}
}
